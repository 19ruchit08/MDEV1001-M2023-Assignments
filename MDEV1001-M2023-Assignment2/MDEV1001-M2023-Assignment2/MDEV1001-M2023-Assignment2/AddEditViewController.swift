import UIKit
import CoreData

class AddEditViewController: UIViewController
{
    // UI References
    @IBOutlet weak var AddEditTitleLabel: UILabel!
    @IBOutlet weak var UpdateButton: UIButton!
    
    // Movie Fields
    @IBOutlet weak var titleTextField: UITextField!
    @IBOutlet weak var studioTextField: UITextField!
    @IBOutlet weak var criticsRatingTextField: UITextField!
    
    var movie: Movie?
    var dataViewController: DataViewController?
    
    override func viewDidLoad()
    {
        super.viewDidLoad()
        
        if let movie = movie
        {
            // Editing existing movie
            titleTextField.text = movie.title
            studioTextField.text = movie.studio
            criticsRatingTextField.text = "\(movie.criticsrating)"
        }
        else
        {
            AddEditTitleLabel.text = "Add Movie"
            UpdateButton.setTitle("Add", for: .normal)
        }
    }
    
    @IBAction func CancelButton_Pressed(_ sender: UIButton)
    {
        dismiss(animated: true, completion: nil)
    }
    
    @IBAction func UpdateButton_Pressed(_ sender: UIButton)
    {
        // Retrieve the app delegate
        guard let appDelegate = UIApplication.shared.delegate as? AppDelegate else
        {
            return
        }

        // Retrieve the managed object context
        let context = appDelegate.persistentContainer.viewContext

        if let movie = movie
        {
            // Editing existing movie
            movie.title = titleTextField.text
            movie.studio = studioTextField.text
            movie.criticsrating = Float(criticsRatingTextField.text ?? "") ?? 0.0
        } else {
            // Creating a new movie
            let newMovie = Movie(context: context)
            newMovie.title = titleTextField.text
            newMovie.studio = studioTextField.text
            newMovie.criticsrating = Float(criticsRatingTextField.text ?? "") ?? 0.0
        }

        // Save the changes in the context
        do {
            try context.save()
            dataViewController?.fetchData()
            dismiss(animated: true, completion: nil)
        } catch {
            print("Failed to save data: \(error)")
        }
    }
    
}
