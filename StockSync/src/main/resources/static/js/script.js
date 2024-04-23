const searchSubmitButton = document.querySelector("#searchSubmit");
const searchItemSubmitButton = document.querySelector("#searchItemSubmit");

searchSubmitButton && searchSubmitButton.addEventListener('click', handleSearchSubmit);

searchItemSubmitButton && searchItemSubmitButton.addEventListener('click', handleSearchItemSubmit);

// get reference to the delete selected button
const deleteSelectedButton = document.querySelector("#deleteSelected");
// attach event listener to the delete selected button
deleteSelectedButton && deleteSelectedButton.addEventListener('click', handleDeleteSelected);

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
    // loop through checkboxes to find checked ones
    for (let i = 0; i < checkboxes.length; i++) {
        // if checkbox is checked, add its warehouse id to selectedIds array
        if (checkboxes[i].checked) {
            selectedIds.push(checkboxes[i].value);
        }
    }
    // set the value of hidden input field with selected warehouse IDs
    document.getElementById('selectedIds').value = selectedIds.join(',');

    //use fetch to handle api requests here and get rid of form in html

    // submit the form for deletion
    document.getElementById('deleteForm').submit();
}

function handleSearchItemSubmit() {
    const searchValue = document.querySelector("#itemSearchInput").value;
    const searchKey = document.querySelector("#itemSearchKey").value;
    const sortBy = document.querySelector("#itemSortBy").value;
    const sortMethod = document.querySelector("#itemSortMethod").value;
    location.href = `/itemSearchResults?page=1&sortBy=${sortBy}&sortMethod=${sortMethod}&searchKey=${searchKey}&searchValue=${searchValue}`
}
