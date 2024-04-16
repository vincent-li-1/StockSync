document.addEventListener('DOMContentLoaded', function() {
    // Code for handling the warehouse form submission
    const addWarehouseForm = document.getElementById('addWarehouseForm');
    // Add an event listener for 'click' events on the form submission button
    if (addWarehouseForm) {
        addWarehouseForm.addEventListener('click', function(e) {
            e.preventDefault(); // Prevent the form from submitting the traditional way (no page reload)
            const formData = new FormData(addWarehouseForm); //Create a FormData object from the form, which allows key-value pairs to be easily sent via HTTP
            fetch('/insertWarehouse', { // Send the form data to the server using the Fetch API
                method: 'POST',// Specify the HTTP method to use
                body: formData // Attach the form data to the body of the request
            })
            .then(response => {
                if (!response.ok) { // If the HTTP response status is not 'ok', throw an error
                    throw new Error('Network response was not ok');
                }
                return response.json(); // Otherwise, parse the response body as JSON
            })
            .then(data => {
                if (data.success) {// If the server response contains a success state
                    alert(data.message); // Show success message
                    addWarehouseForm.reset(); // Reset the form fields to their default values
                } else {
                    showError(data.message); // Show error message
                }
            })
            .catch(error => {
                showError('An error occurred: ' + error.message); // If there's an error during fetch, display it 
            });
        });
    }
    // Code for handling search submission
    const searchSubmitButton = document.querySelector("#searchSubmit");
    // Check if the search button exists
    if (searchSubmitButton) {
        searchSubmitButton.addEventListener('click', handleSearchSubmit);// Add an event listener for 'click' events on the search button
    }
});
//a function to handle the search submission
function handleSearchSubmit() {
    // Retrieve the search values from the input fields
    const searchValue = document.querySelector("#searchinput").value;
    const searchKey = document.querySelector("#searchKey").value;
    const sortBy = document.querySelector("#sortBy").value;
    const sortMethod = document.querySelector("#sortMethod").value;
    // Use fetch to send a search request to the server
    fetch(`/warehouseSearchResults?page=1&sortBy=${sortBy}&sortMethod=${sortMethod}&searchKey=${searchKey}&searchValue=${searchValue}`)
        .then(response => {
            if (!response.ok) {// If the response is not 'ok', throw an error
                throw new Error('Network response was not ok');
            }
            // Redirect the browser to the URL provided in the response
            location.href = response.url;
        })
        .catch(error => {
            showError('An error occurred: ' + error.message); // If there's an error during fetch, display it
        });
}
function showError(message) {
    // Use an alert to show the error message to the user
    alert(message);
}