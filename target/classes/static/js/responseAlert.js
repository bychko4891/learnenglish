function showToast(message, type) {
    var toastContainer = document.getElementById('toastContainer');
    var toast = document.createElement('div');
    toast.className = 'toast';

    if (type === 'success') {
        toast.className += ' toast-success';
    } else if (type === 'error') {
        toast.className += ' toast-error';
    }

    toast.innerHTML = message;
    toastContainer.appendChild(toast);

    $(toast).fadeIn();

    setTimeout(function() {
        $(toast).fadeOut(function() {
            $(this).remove();
        });
    }, 7000);
}

function showSuccessToast(message) {
    showToast(message, 'success');
}

function showErrorToast(message) {
    showToast(message, 'error');
}

// function showAlert(message, type) {
//     var alertDiv = document.createElement("div");
//     alertDiv.classList.add("alert");
//
//     if (type === "success") {
//         alertDiv.classList.add("alert-success");
//     } else if (type === "error") {
//         alertDiv.classList.add("alert-danger");
//     }
//
//     alertDiv.setAttribute("role", "alert");
//
//     var closeButton = document.createElement("button");
//     closeButton.classList.add("btn-close");
//     closeButton.setAttribute("type", "button");
//     closeButton.setAttribute("data-bs-dismiss", "alert");
//     closeButton.setAttribute("aria-label", "Close");
//
//     alertDiv.innerHTML = "<strong>" + message + "</strong>";
//
//     alertDiv.appendChild(closeButton);
//
//     var container = document.getElementById("alertContainer");
//     container.appendChild(alertDiv);
// }