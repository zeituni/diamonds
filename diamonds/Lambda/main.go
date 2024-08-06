package main

import (
	"context"
	"database/sql"
	"encoding/json"
	"fmt"
	"log"
	"os"

	"github.com/aws/aws-lambda-go/events"
	"github.com/aws/aws-lambda-go/lambda"
	_ "github.com/go-sql-driver/mysql"
)

// Request represents the incoming JSON payload
type Request struct {
	StoreID      string `json:"store_id"`
	PhoneNo      string `json:"phone_no"`
	CustomerName string `json:"customer_name"`
	Email        string `json:"email"`
	SKU          string `json:"sku"`
}

// Response represents the Lambda function response
type Response struct {
	Message string `json:"message"`
}

func handler(ctx context.Context, event events.APIGatewayProxyRequest) (events.APIGatewayProxyResponse, error) {
	// Parse the incoming JSON payload
	var req Request
	var requestBody = []byte(event.Body)
	err := json.Unmarshal(requestBody, &req)
	if err != nil {
		log.Printf("Error parsing request body: %v", err)
		return events.APIGatewayProxyResponse{
			StatusCode: 400,
			Body:       fmt.Sprintf("Invalid request body: %v", err),
		}, nil
	}
	log.Printf("Finished parsing request body")
	// Retrieve database connection info from environment variables
	dbUser := os.Getenv("DB_USER")
	dbPassword := os.Getenv("DB_PASSWORD")
	dbHost := os.Getenv("DB_HOST")
	dbPort := os.Getenv("DB_PORT")
	dbName := os.Getenv("DB_NAME")

	// Create a connection string
	connectionString := fmt.Sprintf("%s:%s@tcp(%s:%s)/%s", dbUser, dbPassword, dbHost, dbPort, dbName)
	// Open a connection to the database
	db, err := sql.Open("mysql", connectionString)
	if err != nil {
		log.Printf("Error opening database: %v", err)
		return events.APIGatewayProxyResponse{
			StatusCode: 500,
			Body:       fmt.Sprintf("Error opening database: %v", err),
		}, nil
	}
	defer db.Close()
    var manufacturer int32
    var storeContact int32
    // fetch data from the DB
    err = db.QueryRow(`select store_contact, manufacturer from store s where s.externalId = ?`, req.StoreID).Scan(&storeContact, &manufacturer)
    switch {
    	case err == sql.ErrNoRows:
    		log.Printf("Store was not found %s\n", req.StoreID)
    		return events.APIGatewayProxyResponse{
            			StatusCode: 500,
            			Body:       fmt.Sprintf("No Store was found for external id %s: %v", req.StoreID, err),
            		}, nil
    	case err != nil:
    		log.Fatalf("query error: %v\n", err)
    		return events.APIGatewayProxyResponse{
                        StatusCode: 500,
                        Body:       fmt.Sprintf("Query Error!: %v", err),
                        }, nil
    	default:
    		log.Printf("Found manufacturer %d and contact %d for store %s\n", manufacturer, storeContact, req.StoreID)
    	}
    var jewelryId int32
    err = db.QueryRow(`select id from jewelry where barcode = ? and manufacturer = 38`, req.SKU).Scan(&jewelryId)
        switch {
        	case err == sql.ErrNoRows:
        		log.Printf("SKU was not found for Luna Collection %s\n", req.SKU)
        		return events.APIGatewayProxyResponse{
                			StatusCode: 500,
                			Body:       fmt.Sprintf("No SKU was found for %s: %v", req.SKU, err),
                		}, nil
        	case err != nil:
        		log.Fatalf("query error: %v\n", err)
        		return events.APIGatewayProxyResponse{
                            StatusCode: 500,
                            Body:       fmt.Sprintf("Query Error!: %v", err),
                            }, nil
        	default:
        		log.Printf("Found item %d for SKU  %s\n", jewelryId, req.SKU)
        	}
    // Insert the data into the database
	query := "INSERT INTO customer (phone, email, name, manufacturer, sales_person, jewelry)\n " +
	"VALUES (?, ?, ?, ?, ?, ?)"

	_, err = db.Exec(query, req.PhoneNo, req.Email, req.CustomerName, manufacturer, storeContact, jewelryId)
	if err != nil {
		log.Printf("Error inserting data into database: %v", err)
		return events.APIGatewayProxyResponse{
			StatusCode: 500,
			Body:       fmt.Sprintf("Error inserting data into database: %v", err),
		}, nil
	}

	// Return a success message
	return events.APIGatewayProxyResponse{
		StatusCode: 200,
		Body:       fmt.Sprintf("Data inserted successfully for customer: %s", req.CustomerName),
	}, nil
}

func main() {
	lambda.Start(handler)
}
