const searchSubmitButton = document.querySelector("#searchSubmit");
const searchItemSubmitButton = document.querySelector("#searchItemSubmit");

searchSubmitButton && searchSubmitButton.addEventListener('click', handleSearchSubmit);

searchItemSubmitButton && searchItemSubmitButton.addEventListener('click', handleSearchItemSubmit);

function handleSearchSubmit() {
    const searchValue = document.querySelector("#searchinput").value;
    const searchKey = document.querySelector("#searchKey").value;
    const sortBy = document.querySelector("#sortBy").value;
    const sortMethod = document.querySelector("#sortMethod").value;
    location.href = `/warehouseSearchResults?page=1&sortBy=${sortBy}&sortMethod=${sortMethod}&searchKey=${searchKey}&searchValue=${searchValue}`
}

function handleSearchItemSubmit() {
    const searchValue = document.querySelector("#itemSearchInput").value;
    const searchKey = document.querySelector("#itemSearchKey").value;
    const sortBy = document.querySelector("#itemSortBy").value;
    const sortMethod = document.querySelector("#itemSortMethod").value;
    location.href = `/itemSearchResults?page=1&sortBy=${sortBy}&sortMethod=${sortMethod}&searchKey=${searchKey}&searchValue=${searchValue}`
}
