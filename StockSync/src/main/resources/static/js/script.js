document.addEventListener('DOMContentLoaded', function() {
    const addWarehouseForm = document.getElementById('addWarehouseForm');
    const searchForm = document.getElementById('searchForm');
    const searchSubmitButton = document.querySelector("#searchSubmit");
    const searchItemSubmitButton = document.querySelector("#searchItemSubmit");
    const addItemForm = document.getElementById('addItemForm');

    // get reference to the delete selected button
    const deleteSelectedButton = document.querySelector("#deleteSelected");
    // attach event listener to the delete selected button
    deleteSelectedButton && deleteSelectedButton.addEventListener('click', handleDeleteSelected);
    
// get reference to the delete selected button
const deleteSelectedItemButton = document.querySelector("#deleteSelectedItems");
// attach event listener to the delete selected button
deleteSelectedItemButton && deleteSelectedItemButton.addEventListener('click', handleDeleteSelectedItem);


searchItemSubmitButton && searchItemSubmitButton.addEventListener('click', handleSearchItemSubmit);
    searchForm && searchForm.addEventListener('submit', handleSearchSubmit); // Attach to the form's submit event


    // Helper function to display error messages
    function showErrorPopup(message) {
        alert(message); // Using an alert box to display the error
    }

/*
* function to handle deletion of all selected warehouses
*/
function handleDeleteSelectedItem() {
    // array to store IDs of selected warehouses
    const selectedItems = [];
    // get all checkboxes with class warehouseCheckbox
    const checkboxes = document.getElementsByClassName('itemCheckbox');
    console.log(checkboxes)
    // loop through checkboxes to find checked ones
    for (let i = 0; i < checkboxes.length; i++) {
        // if checkbox is checked, add its warehouse id to selectedIds array
        if (checkboxes[i].checked) {
            selectedItems.push(checkboxes[i].value);
        }
    }
    console.log(selectedItems)
    console.log(JSON.stringify(selectedItems))
    const response = fetch('/deleteItem', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(selectedItems)
    }).then(response => {
              // Check if the request was successful
              if (response.ok) {
                  // If the server responded with a successful status, redirect to the search results page
                  window.location.href = '/itemSearchResults?page=1';
              } else {
                  // If the server response was not ok (e.g., 400, 500), handle it accordingly
                  console.error('Request failed with status:', response.status);
              }
          })
          .catch(error => {
              console.error('Network error:', error);
          });

}

    /*
    * function to handle deletion of all selected warehouses
    */
    function handleDeleteSelected() {
        //array to store IDs of selected warehouses
        const selectedIds = [];
        // get all checkboxes with class warehouseCheckbox
        const checkboxes = document.getElementsByClassName('warehouseCheckbox');
        console.log(checkboxes)
        // loop through checkboxes to find checked ones
        for (let i = 0; i < checkboxes.length; i++) {
            // if checkbox is checked, add its warehouse id to selectedIds array
            if (checkboxes[i].checked) {
                selectedIds.push(checkboxes[i].value);
            }
        }
        console.log(selectedIds)
        console.log(JSON.stringify(selectedIds))
        const response = fetch('/deleteWarehouse', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(selectedIds)
        }).then(response => {
                // Check if the request was successful
                if (response.ok) {
                    // If the server responded with a successful status, redirect to the search results page
                    window.location.href = '/warehouseSearchResults?page=1';
                } else {
                    // If the server response was not ok (e.g., 400, 500), handle it accordingly
                    console.error('Request failed with status:', response.status);
                }
            })
            .catch(error => {
                console.error('Network error:', error);
            });

    }
    function handleSearchItemSubmit() {
        const searchValue = document.querySelector("#itemSearchInput").value;
        const searchKey = document.querySelector("#itemSearchKey").value;
        const sortBy = document.querySelector("#itemSortBy").value;
        const sortMethod = document.querySelector("#itemSortMethod").value;
        location.href = `/itemSearchResults?page=1&sortBy=${sortBy}&sortMethod=${sortMethod}&searchKey=${searchKey}&searchValue=${searchValue}`
    }
    // Function to handle the warehouse form submission
    function handleWarehouseFormSubmit(event) {
        event.preventDefault(); // Prevent the default form submission
    
        // Validate longitude and latitude values
        const longitude = parseFloat(document.getElementById('warehouseLong').value);
        const latitude = parseFloat(document.getElementById('warehouseLat').value);
    
        if (isNaN(longitude) || isNaN(latitude)) {
            showErrorPopup('Longitude and latitude must be valid numbers.');
            return;
        }
    
        // If validation passes, proceed with form submission
        const formData = new URLSearchParams();
        for (const pair of new FormData(addWarehouseForm)) {
            formData.append(pair[0], pair[1]);
        }
    
        fetch('/insertWarehouse', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            },
            body: formData
        })
        .then(response => {
            console.log(response);
            if (!response.ok) { // If the response status is not OK, it's an error
                return response.json().then(errorData => Promise.reject(errorData));
            }
            return response.json(); // For a good response, parse it as JSON
        })
        .then(data => {
            if (data.success) { 
                alert(data.message); // Show a success message
                // Optionally, reset the form or handle additional success logic
                addWarehouseForm.reset();
            } else {
                // Handle any other successful response that's not expected
                showErrorPopup('Unexpected success response, no success flag.');
            }
        })
        .catch(error => {
            // Display the error message contained in the response JSON
            showErrorPopup(error.error || 'An error occurred.');
        });
    }

     // Function to handle the warehouse form submission
    function handleWarehouseFormSubmit(event) {
        event.preventDefault(); // Prevent the default form submission
    
        // Validate longitude and latitude values
        const longitude = parseFloat(document.getElementById('warehouseLong').value);
        const latitude = parseFloat(document.getElementById('warehouseLat').value);
    
        if (isNaN(longitude) || isNaN(latitude)) {
            showErrorPopup('Longitude and latitude must be valid numbers.');
            return;
        }
    
        // If validation passes, proceed with form submission
        const formData = new URLSearchParams();
        for (const pair of new FormData(addWarehouseForm)) {
            formData.append(pair[0], pair[1]);
        }
    
        fetch('/insertWarehouse', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            },
            body: formData
        })
        .then(response => {
            console.log(response);
            if (!response.ok) { // If the response status is not OK, it's an error
                return response.json().then(errorData => Promise.reject(errorData));
            }
            return response.json(); // For a good response, parse it as JSON
        })
        .then(data => {
            if (data.success) { 
                alert(data.message); // Show a success message
                // Optionally, reset the form or handle additional success logic
                addWarehouseForm.reset();
            } else {
                // Handle any other successful response that's not expected
                showErrorPopup('Unexpected success response, no success flag.');
            }
        })
        .catch(error => {
            // Display the error message contained in the response JSON
            showErrorPopup(error.error || 'An error occurred.');
        });
    }

    // Function to handle the item form submission
    function handleItemFormSubmit(event) {
        event.preventDefault(); // Prevent the default form submission
    
        // Validate price and size values
        const price = parseFloat(document.getElementById('itemPrice').value);
        const size = parseFloat(document.getElementById('itemSize').value);
    
        if (isNaN(price) || isNaN(size)) {
            showErrorPopup('Longitude and latitude must be valid numbers.');
            return;
        }
    
        // If validation passes, proceed with form submission
        const formData = new URLSearchParams();
        for (const pair of new FormData(addItemForm)) {
            formData.append(pair[0], pair[1]);
        }
    
        fetch('/insertItem', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            },
            body: formData
        })
        .then(response => {
            console.log(response);
            if (!response.ok) { // If the response status is not OK, it's an error
                return response.json().then(errorData => Promise.reject(errorData));
            }
            return response.json(); // For a good response, parse it as JSON
        })
        .then(data => {
            if (data.success) { 
                alert(data.message); // Show a success message
                // Optionally, reset the form or handle additional success logic
                addItemForm.reset();
            } else {
                // Handle any other successful response that's not expected
                showErrorPopup('Unexpected success response, no success flag.');
            }
        })
        .catch(error => {
            // Display the error message contained in the response JSON
            showErrorPopup(error.error || 'An error occurred.');
        });
    }

    function handleSearchSubmit(event) {
        event.preventDefault();
        const searchValue = document.querySelector("#searchinput").value;
        const searchKey = document.querySelector("#searchKey").value;
        const sortBy = document.querySelector("#sortBy").value;
        const sortMethod = document.querySelector("#sortMethod").value;
    
        fetch(`/warehouseSearchResults?page=1&sortBy=${sortBy}&sortMethod=${sortMethod}&searchKey=${searchKey}&searchValue=${searchValue}`)
        .then(response => {
            // Check if the response is JSON
            const contentType = response.headers.get('content-type');
            if (contentType && contentType.indexOf('application/json') !== -1) {
                return response.json().then(data => {
                    if (!data.success) {
                        showErrorPopup(data.message);
                    } else {
                        window.location.href = data.redirectURL;
                    }
                });
            } else {
                // If it's not JSON, you can assume it's HTML
                // You might want to handle this case differently,
                // for example by displaying the HTML content.
                // For now redirect to the response URL.
                window.location.href = response.url;
            }
        })
        .catch(error => {
            showErrorPopup('An error occurred: ' + error.message);
        });
    }

    // Attach the event listener for the warehouse form
    if (addWarehouseForm) {
        addWarehouseForm.addEventListener('submit', handleWarehouseFormSubmit);
    }

    if (addItemForm) {
        addItemForm.addEventListener('submit', handleItemFormSubmit);
    }
});