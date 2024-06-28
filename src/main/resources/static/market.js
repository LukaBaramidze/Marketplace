let currentPage = 0;
const pageSize = 6;

async function fetchAndDisplayListings(pageNum, pageSize) {
  const response = await fetch(`http://localhost:8080/market?pageNum=${pageNum}&pageSize=${pageSize}`);

  if (!response.ok) {
    console.error(`HTTP error! Status: ${response.status}`);
    return;
  }

  const data = await response.json();
  const listings = data.dtoCollection || [];
  const listingsContainer = document.getElementById('listingsContainer');
  const pageInfo = document.getElementById('pageInfo');

  listingsContainer.innerHTML = '';

  listings.forEach(listing => {
    const listingElement = document.createElement('div');
    listingElement.classList.add('listing');

    const listingDetails = `
      <h2>${listing.name}</h2>
      <p>Price: ${listing.price}</p>
      <img src="${listing.photoUrl}" alt="Listing Photo">
      <a href="item.html?name=${listing.name}">View Details</a>
    `;
    listingElement.innerHTML = listingDetails;

    listingsContainer.appendChild(listingElement);
  });

  pageInfo.textContent = `Page ${pageNum + 1}`;

  prevButton.disabled = pageNum === 0;
  nextButton.disabled = listings.length < pageSize;
}

function changePage(direction) {
  currentPage += direction;
  fetchAndDisplayListings(currentPage, pageSize);
}

async function addListing() {
  const formData = new FormData();
  const mhm = document.getElementById('photo');
  console.log(mhm + "yeahhh");
  formData.append('name', document.getElementById('name').value);
  formData.append('price', document.getElementById('price').value);
  formData.append('description', document.getElementById('description').value);

  console.log('Form data:', formData);

  const response = await fetch('http://localhost:8080/market', {
    method: 'POST',
    body: JSON.stringify({
      name: formData.get('name'),
      price: parseFloat(formData.get('price')),
      description: formData.get('description')
    }),
    headers: {
      'Content-Type': 'application/json'
    }
  });

  if (!response.ok) {
    console.error(`HTTP error! Status: ${response.status}`);
    return;
  }

  console.log('Listing added successfully!');

  const imageInput = document.getElementById('photo');
  if (imageInput.files.length > 0) {
    const photoFormData = new FormData();
    photoFormData.append('photo', imageInput.files[0]);
    photoFormData.append('name', formData.get('name'));

    console.log('Photo form data:', photoFormData);

    try {
      const photoResponse = await fetch('http://localhost:8080/photo', {
        method: 'POST',
        body: photoFormData
      });

      if (!photoResponse.ok) {
        console.error(`HTTP error! Status: ${photoResponse.status}`);
        return;
      }

      console.log('Photo uploaded successfully!');
    } catch (error) {
      console.error('Error uploading photo:', error);
    }
  }
}


async function fetchAndDisplayListingDetails(name) {
  const response = await fetch(`http://localhost:8080/market/${name}`);

  if (!response.ok) {
    console.error(`HTTP error! Status: ${response.status}`);
    return;
  }

  const data = await response.json();
  const listing = data || {};

  const modalListingName = document.getElementById('modalListingName');
  const modalListingPrice = document.getElementById('modalListingPrice');
  const modalListingDescription = document.getElementById('modalListingDescription');
  const modalListingPhoto = document.getElementById('modalListingPhoto');

  modalListingName.textContent = listing.name;
  modalListingPrice.textContent = `Price: ${listing.price}`;
  modalListingDescription.textContent = listing.description;
  modalListingPhoto.src = listing.photoUrl;
}

async function fetchAllPhotos() {
  const response = await fetch('http://localhost:8080/photo');

  if (!response.ok) {
    console.error(`HTTP error! Status: ${response.status}`);
    return;
  }

  const data = await response.json();
  const photos = data || [];
  console.log(photos);
}

window.onload = function () {
    const urlParams = new URLSearchParams(window.location.search);
    const itemName = urlParams.get('name');
    if (itemName) {
        fetchAndDisplayListingDetails(itemName);
    } else {
        fetchAndDisplayListings(currentPage, pageSize);
    }
};
