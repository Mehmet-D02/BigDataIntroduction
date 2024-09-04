package com.bigdatacompany.mongdodbent;

import com.mongodb.BasicDBObject;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoException;
import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Filter;

public class Application {
    public static void main(String[] args) {

        String connectionString = "mongodb+srv://mehmet:12345@cluster0.14xh2.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0";

        MongoClient client = MongoClients.create(connectionString);

        MongoDatabase infoDB = client.getDatabase("Info");
        MongoCollection<Document> personalCollection = infoDB.getCollection("Personal");

        //MongoDB CRUD PROCESS

        BasicDBObject data = new BasicDBObject()
                .append("name", "Thomas Edison")
                .append("date",1847)
                .append("country","USA");

        BasicDBObject data2 = new BasicDBObject()
                .append("name", "Elon Musk")
                .append("date",1971)
                .append("country","Africa")
                .append("Job","Entrepreneur");

        //Creating Process
         personalCollection.insertOne(Document.parse(data.toJson())); //Add one document

        Document parse = Document.parse(data.toJson());
        Document parse2 = Document.parse(data2.toJson());
        personalCollection.insertMany(Arrays.asList(parse,parse2)); //Add Many document

        //Reading Process
        FindIterable<Document> documents = personalCollection.find(); //All Document Reading
        FindIterable<Document> documents1 = personalCollection.find(new BasicDBObject("date", 1999)); // Filtering

        for(Document doc:documents){
            System.out.println(doc.toJson());
        };
        for (Document doc1:documents1){
            System.out.println(doc1.toJson());
        }

        //Update Process
        Bson filter = Filters.exists( "Job");
        Bson update = Updates.set("child", "Nevada Musk");
        personalCollection.updateOne(filter,update);

        //Delete Process
        Bson deleteFilter = Filters.eq("country","England");
        personalCollection.deleteOne(deleteFilter); //Delete One items
        personalCollection.deleteMany(deleteFilter); //Delete  Many Items 


        personalCollection.drop(); //Delete Collection
        infoDB.drop(); //Delete Database
    }
}

