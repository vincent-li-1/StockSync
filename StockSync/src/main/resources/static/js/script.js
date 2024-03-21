const searchSubmitButton = document.querySelector("#searchSubmit");

searchSubmitButton && searchSubmitButton.addEventListener('click', handleSearchSubmit);

function handleSearchSubmit() {
    const searchInput = document.querySelector("#searchinput").value;
    const sortBy = document.querySelector("#sortBy").value;
    const sortMethod = document.querySelector("#sortMethod").value;
    location.href = `/warehouseSearchResults?page=1&sortBy=${sortBy}&sortMethod=${sortMethod}`
}