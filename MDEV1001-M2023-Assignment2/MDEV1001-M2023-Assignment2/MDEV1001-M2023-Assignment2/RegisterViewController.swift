import UIKit
import CoreData

class RegisterViewController: UIViewController
{
    // Connect TextFields
    @IBOutlet weak var firstNameTextField: UITextField!
    @IBOutlet weak var lastNameTextField: UITextField!
    @IBOutlet weak var emailTextField: UITextField!
    @IBOutlet weak var usernameTextField: UITextField!
    @IBOutlet weak var passwordTextField: UITextField!
    @IBOutlet weak var confirmPasswordTextField: UITextField!
    
    override func viewDidLoad()
    {
        super.viewDidLoad()
    }
    
    @IBAction func registerButton_Pressed(_ sender: UIButton)
    {
        guard let appDelegate = UIApplication.shared.delegate as? AppDelegate else
        {
            return
        }
        
        let context = appDelegate.persistentContainer.viewContext
        
        guard let username = usernameTextField.text,
              let password = passwordTextField.text,
              let email = emailTextField.text,
              let firstName = firstNameTextField.text,
              let lastName = lastNameTextField.text,
              !username.isEmpty, !password.isEmpty, !email.isEmpty else {
            // Display an alert or error message if any field is empty
            return
        }
        
        // Check if a user with the same username or email already exists
        let fetchRequest: NSFetchRequest<User> = User.fetchRequest()
        fetchRequest.predicate = NSPredicate(format: "username = %@ OR email = %@", username, email)
        
        do {
            let users = try context.fetch(fetchRequest)
            
            if users.isEmpty
            {
                // Validate password confirmation
                guard let confirmPassword = confirmPasswordTextField.text, password == confirmPassword else {
                // Display an error message and navigate to the password text field
                let alert = UIAlertController(title: "Registration Failed", message: "Passwords do not match.", preferredStyle: .alert)
                    alert.addAction(UIAlertAction(title: "OK", style: .default, handler: { _ in
                        self.passwordTextField.text = ""
                        self.confirmPasswordTextField.text = ""
                        self.passwordTextField.becomeFirstResponder()
                    }))
                present(alert, animated: true, completion: nil)
                return
            }
                
                // Create a new User object
                let newUser = User(context: context)
                // Set the attributes of the new User object
                newUser.username = username
                newUser.password = password
                newUser.email = email
                newUser.firstname = firstName
                newUser.lastname = lastName
                
                try context.save()
                print("Successfully Registered User")
                
                // Clear the username and password in the LoginViewController
                LoginViewController.shared?.ClearLoginTextFields()
                
                // Registration successful, navigate back to the LoginViewController
                dismiss(animated: true, completion: nil)
            } else {
                // User with the same username or email already exists, display an error message
                let alert = UIAlertController(title: "Registration Failed", message: "A user with the same username or email already exists.", preferredStyle: .alert)
                alert.addAction(UIAlertAction(title: "OK", style: .default, handler: nil))
                present(alert, animated: true, completion: nil)
            }
            
        } catch {
            print("Failed to fetch user: \(error)")
        }
    }
    
    @IBAction func CancelButton_Pressed(_ sender: UIButton)
    {
        // Clear the username and password in the LoginViewController
        LoginViewController.shared?.ClearLoginTextFields()
        dismiss(animated: true, completion: nil)
    }
}
