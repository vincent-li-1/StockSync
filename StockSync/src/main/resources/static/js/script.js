document.addEventListener('DOMContentLoaded', function() {
    // Code for handling the warehouse form submission
    const addWarehouseForm = document.getElementById('addWarehouseForm');
    if (addWarehouseForm) {
        addWarehouseForm.addEventListener('click', function(e) {
            e.preventDefault(); // Prevent the default form submission

            const formData = new FormData(addWarehouseForm);

            fetch('/insertWarehouse', {
                method: 'POST',
                body: formData
            })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.json();
            })
            .then(data => {
                if (data.success) {
                    alert(data.message); // Show success message
                    addWarehouseForm.reset(); // Reset form
                } else {
                    showError(data.message); // Show error message
                }
            })
            .catch(error => {
                showError('An error occurred: ' + error.message); // Show error message
            });
        });
    }
    
    // Code for handling search submission
    const searchSubmitButton = document.querySelector("#searchSubmit");
    if (searchSubmitButton) {
        searchSubmitButton.addEventListener('click', handleSearchSubmit);
    }
});

function handleSearchSubmit() {
    const searchValue = document.querySelector("#searchinput").value;
    const searchKey = document.querySelector("#searchKey").value;
    const sortBy = document.querySelector("#sortBy").value;
    const sortMethod = document.querySelector("#sortMethod").value;

    fetch(`/warehouseSearchResults?page=1&sortBy=${sortBy}&sortMethod=${sortMethod}&searchKey=${searchKey}&searchValue=${searchValue}`)
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            // Redirect to search results page
            location.href = response.url;
        })
        .catch(error => {
            showError('An error occurred: ' + error.message); // Show error message
        });
}

function showError(message) {
    // Display error message on the page
    alert(message);
}