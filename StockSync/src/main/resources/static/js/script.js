const searchSubmitButton = document.querySelector("#searchSubmit");
const searchItemSubmitButton = document.querySelector("#searchItemSubmit");

searchSubmitButton && searchSubmitButton.addEventListener('click', handleSearchSubmit);

searchItemSubmitButton && searchItemSubmitButton.addEventListener('click', handleSearchItemSubmit);

// get reference to the delete selected button
const deleteSelectedButton = document.querySelector("#deleteSelected");
// attach event listener to the delete selected button
deleteSelectedButton && deleteSelectedButton.addEventListener('click', handleDeleteSelected);
searchItemSubmitButton && searchItemSubmitButton.addEventListener('click', handleSearchItemSubmit);

function handleSearchSubmit() {
    const searchValue = document.querySelector("#searchinput").value;
    const searchKey = document.querySelector("#searchKey").value;
    const sortBy = document.querySelector("#sortBy").value;
    const sortMethod = document.querySelector("#sortMethod").value;
    location.href = `/warehouseSearchResults?page=1&sortBy=${sortBy}&sortMethod=${sortMethod}&searchKey=${searchKey}&searchValue=${searchValue}`
}

function handleEditSubmit() {
    
}

/*
* function to handle deletion of all selected warehouses
*/
function handleDeleteSelected() {
    // array to store IDs of selected warehouses
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
