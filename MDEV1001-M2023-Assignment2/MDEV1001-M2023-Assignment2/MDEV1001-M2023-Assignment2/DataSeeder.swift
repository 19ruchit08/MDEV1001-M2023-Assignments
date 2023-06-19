import Foundation
import UIKit
import CoreData

func seedData() {
    guard let url = Bundle.main.url(forResource: "movies", withExtension: "json") else {
        print("JSON file not found.")
        return
    }
    
    guard let data = try? Data(contentsOf: url) else {
        print("Failed to read JSON file.")
        return
    }
    
    guard let jsonArray = try? JSONSerialization.jsonObject(with: data, options: []) as? [[String: Any]] else {
        print("Failed to parse JSON.")
        return
    }
    
    guard let appDelegate = UIApplication.shared.delegate as? AppDelegate else {
        print("AppDelegate not found.")
        return
    }
    
    let context = appDelegate.persistentContainer.viewContext
    
    for jsonObject in jsonArray {
        let movie = Movie(context: context)
        
        movie.movieID = jsonObject["movieID"] as? Int16 ?? 0
        movie.title = jsonObject["title"] as? String
        movie.studio = jsonObject["studio"] as? String
        movie.criticsrating = jsonObject["criticsRating"] as? Float ?? 0.0
        
        // Save the context after each movie is created
        do {
            try context.save()
        } catch {
            print("Failed to save movie: \(error)")
        }
    }
    
    print("Data seeded successfully.")
}
