import UIKit
import CoreData

class DataViewController: UIViewController, UITableViewDelegate, UITableViewDataSource
{
  
    let Myimage = [UIImage(named: "star wars empire strikes back 1.jpg"),UIImage(named:"The Matrix 2.jpg")]
    
    @IBOutlet weak var tableView: UITableView!
    
    var movies: [Movie] = []
          
    override func viewDidLoad()
    {
        super.viewDidLoad()
        fetchData()
    }
        
    func fetchData()
    {
        guard let appDelegate = UIApplication.shared.delegate as? AppDelegate else
        {
            return
        }
                
        let context = appDelegate.persistentContainer.viewContext
                
        let fetchRequest: NSFetchRequest<Movie> = Movie.fetchRequest()
                
                do {
                    movies = try context.fetch(fetchRequest)
                    tableView.reloadData()
                } catch {
                    print("Failed to fetch data: \(error)")
                }
            }
        
        func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
                return movies.count
            }
      
    // Updated for ICE5
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell
    {
        let cell = tableView.dequeueReusableCell(withIdentifier: "MovieCell", for: indexPath) as! MovieTableViewCell
                
        let movie = movies[indexPath.row]
                
        cell.titleLabel?.text = movie.title
        cell.studioLabel?.text = movie.studio
        cell.ratingLabel?.text = "\(movie.criticsrating)"
        cell.MyimageView?.image = Myimage[indexPath.row]
        
        // Set the background color of criticsRatingLabel based on the rating
        let rating = movie.criticsrating
                   
        if rating > 7
        {
            cell.ratingLabel.backgroundColor = UIColor.green
            cell.ratingLabel.textColor = UIColor.black
        } else if rating > 5 {
            cell.ratingLabel.backgroundColor = UIColor.yellow
            cell.ratingLabel.textColor = UIColor.black
        } else {
            cell.ratingLabel.backgroundColor = UIColor.red
            cell.ratingLabel.textColor = UIColor.white
        }
        return cell
    }
    
    // New for ICE5
        func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath)
        {
            performSegue(withIdentifier: "AddEditSegue", sender: indexPath)
        }
        
    // Swipe Left Gesture
        func tableView(_ tableView: UITableView, commit editingStyle: UITableViewCell.EditingStyle, forRowAt indexPath: IndexPath)
        {
            if editingStyle == .delete
            {
                let movie = movies[indexPath.row]
                ShowDeleteConfirmationAlert(for: movie) { confirmed in
                    if confirmed
                    {
                        self.deleteMovie(at: indexPath)
                    }
                }
            }
        }
        
        @IBAction func AddButton_Pressed(_ sender: UIButton)
        {
            performSegue(withIdentifier: "AddEditSegue", sender: nil)
        }
        
        override func prepare(for segue: UIStoryboardSegue, sender: Any?)
        {
            if segue.identifier == "AddEditSegue"
            {
                if let addEditVC = segue.destination as? AddEditViewController
                {
                    addEditVC.dataViewController = self
                    if let indexPath = sender as? IndexPath
                    {
                       // Editing existing movie
                       let movie = movies[indexPath.row]
                       addEditVC.movie = movie
                    } else {
                        // Adding new movie
                        addEditVC.movie = nil
                    }
                }
            }
        }
        
        func ShowDeleteConfirmationAlert(for movie: Movie, completion: @escaping (Bool) -> Void)
        {
            let alert = UIAlertController(title: "Delete Movie", message: "Are you sure you want to delete this movie?", preferredStyle: .alert)
            
            alert.addAction(UIAlertAction(title: "Cancel", style: .cancel) { _ in
                completion(false)
            })
            
            alert.addAction(UIAlertAction(title: "Delete", style: .destructive) { _ in
                completion(true)
            })
            
            present(alert, animated: true, completion: nil)
        }
        
        func deleteMovie(at indexPath: IndexPath)
        {
            let movie = movies[indexPath.row]
            
            guard let appDelegate = UIApplication.shared.delegate as? AppDelegate else
            {
                return
            }
            
            let context = appDelegate.persistentContainer.viewContext
            context.delete(movie)
            
            do {
                try context.save()
                movies.remove(at: indexPath.row)
                tableView.deleteRows(at: [indexPath], with: .fade)
            } catch {
                print("Failed to delete movie: \(error)")
            }
        }
    
}
