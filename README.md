# My Version of MVVM

## Purpose
This codebase provides a sample of my particular style of MVVM. Whle most projects I have seen use architecture patterns in the project,
A good amount of those projects did not have a clear pattern regarding the structure of the code, this codebase is my personal
preference regarding a cleal, testable structure.

### Structure
Their are four main classes per activity/fragment
> The View Class:
* The actual fragment/activity class
> The ViewModel Class:
* handles getters and setters
* handles onClick listners via databinding
* handles all business logic of the view
* handles dataModel requests
> The DataModel Class:
* Handles all API requests only
> The Callbacks Interface
* Used by the ViewModel and DataModel to handle events on the View

