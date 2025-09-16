console.log("contact.js loaded");
const baseUrl = "http://localhost:8080";
const viewContactModal = document.getElementById("view_contact_modal");
// options with default values
const options = {
    placement: 'bottom-right',
    backdrop: 'dynamic',
    backdropClasses:
        'bg-gray-900/50 dark:bg-gray-900/80 fixed inset-0 z-40',
    closable: true,
    onHide: () => {
        console.log('modal is hidden');
    },
    onShow: () => {
        console.log('modal is shown');
    },
    onToggle: () => {
        console.log('modal has been toggled');
    },
};

// instance options object
const instanceOptions = {
  id: 'view_contact_modal',
  override: true
};


const contactModal = new Modal(viewContactModal, options, instanceOptions);

function openContactModal(){
    contactModal.show();
}

function hideContactModal(){
    contactModal.hide();
}

async function loadConatctData(id){

    console.log(id);

    try {
        const data = await(
        await fetch(`${baseUrl}/api/contacts/${id}`))
        .json();
        document.querySelector("#contact_name").innerText = data.name;
        document.querySelector("#contact_email").innerText = data.email;
        // todo load other data
        openContactModal();
        console.log(data);
  
        
    } catch (error) {
        console.log("Error: ", error);
        
    }

}

// delet contact 

function deleteContact(id){
Swal.fire({
  title: "Do you want to delete the contact?",
  icon: "warning",
  showCancelButton: true,
  confirmButtonText: "Delete",
  
}).then((result) => {
  /* Read more about isConfirmed, isDenied below */
  if (result.isConfirmed) {
    
    const url = `${baseUrl}/user/contacts/delete/` + id;
    window.location.replace(url);
    
  } 
});
}