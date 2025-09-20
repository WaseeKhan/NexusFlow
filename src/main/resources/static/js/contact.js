console.log("contact.js loaded");

const baseUrl = "http://localhost:8080";

// Grab modal element
const viewContactModal = document.getElementById("view_contact_modal");

// Open modal
function openContactModal() {
  if (viewContactModal) {
    viewContactModal.classList.remove("hidden");
    viewContactModal.classList.add("flex");
  }
}

// Hide modal
function hideContactModal() {
  if (viewContactModal) {
    viewContactModal.classList.add("hidden");
    viewContactModal.classList.remove("flex");
  }
}

// Load contact data by ID and open modal
async function loadConatctData(id) {
  console.log("Loading contact:", id);

  try {
    const response = await fetch(`${baseUrl}/api/contacts/${id}`);
    const data = await response.json();

    // Fill modal fields
    document.querySelector("#contact_name").innerText = data.name || "No name";
    document.querySelector("#contact_email").innerText = data.email || "No email";
    document.querySelector("#contact_phone").innerHTML =
      `<i class="fa-solid fa-phone mr-1"></i> ${data.phone || "N/A"}`;
    document.querySelector("#contact_about").innerText = data.about || "";

    // Profile picture
    const picture = document.querySelector("#contact_picture");
    if (picture) {
      picture.src = data.picture || "https://via.placeholder.com/150";
    }

    // Links
    document.querySelector("#contact_linkedin").href = data.linkedin || "#";
    document.querySelector("#contact_website").href = data.website || "#";

    // Open modal
    openContactModal();

    console.log("Contact loaded:", data);

  } catch (error) {
    console.error("Error loading contact:", error);
  }
}

// Delete contact with confirmation
function deleteContact(id) {
  Swal.fire({
    title: "Do you want to delete the contact?",
    icon: "warning",
    showCancelButton: true,
    confirmButtonText: "Delete",
  }).then((result) => {
    if (result.isConfirmed) {
      const url = `${baseUrl}/user/contacts/delete/${id}`;
      window.location.replace(url);
    }
  });
}
