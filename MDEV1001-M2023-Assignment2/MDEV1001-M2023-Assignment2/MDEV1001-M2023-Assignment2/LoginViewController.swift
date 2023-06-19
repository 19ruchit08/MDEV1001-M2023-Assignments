import UIKit
import CoreData

class LoginViewController : UIViewController
{
    // Register TextField Outlets
    @IBOutlet weak var usernameTextField: UITextField!
    @IBOutlet weak var passwordTextField: UITextField!
    
    static var shared: LoginViewController?
    
    override func viewDidLoad()
    {
        super.viewDidLoad()
        LoginViewController.shared = self
    }
    
    func ClearLoginTextFields()
    {
        usernameTextField.text = ""
        passwordTextField.text = ""
        usernameTextField.becomeFirstResponder()
    }

    @IBAction func LoginButton_Pressed(_ sender: UIButton)
    {
        guard let appDelegate = UIApplication.shared.delegate as? AppDelegate else
        {
            return
        }
        
        let context = appDelegate.persistentContainer.viewContext
        
        guard let username = usernameTextField.text,
              let password = passwordTextField.text,
              !username.isEmpty, !password.isEmpty else
        {
            // Display an alert message if any field is empty
            let alert = UIAlertController(title: "Login Failed", message: "username and password are required", preferredStyle: .alert)
            alert.addAction(UIAlertAction(title: "OK", style: .default, handler: nil))
            present(alert, animated: true, completion: nil)
            
            // Shift focus to the empty text field
            if usernameTextField.text?.isEmpty == true
            {
                usernameTextField.becomeFirstResponder()
            }
            else if passwordTextField.text?.isEmpty == true
            {
                passwordTextField.becomeFirstResponder()
            }
            return
        }
        
        // Create a fetch request to check if the user exists
        let fetchRequest: NSFetchRequest<User> = User.fetchRequest()
        fetchRequest.predicate = NSPredicate(format: "username = %@ AND password = %@", username, password)
        
        do {
            let users = try context.fetch(fetchRequest)
            
            if let user = users.first
            {
                print("Login Successful")
                // Successful login
                performSegue(withIdentifier: "LoginSegue", sender: nil)
            } else {
                // Invalid credentials, display an error message
                let alert = UIAlertController(title: "Login Failed", message: "Invalid username or password", preferredStyle: .alert)
                alert.addAction(UIAlertAction(title: "OK", style: .default, handler: nil))
                present(alert, animated: true, completion: nil)
            }
        } catch {
            // Handle the error appropriately (e.g., display an alert)
            print("Failed to fetch user: \(error)")
        }
    }
    
    @IBAction func RegisterButton_Pressed(_ sender: UIButton)
    {
        performSegue(withIdentifier: "RegisterSegue", sender: nil)
    }
}
