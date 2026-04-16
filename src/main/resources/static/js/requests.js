function showWarning(warning) {
  let el = document.getElementById("warning");
  if (el) {
    el.classList.remove('d-none');
    el.innerHTML = warning;
  }
}

async function sendDeleteRequest(path) {

  const csrfHeader = document.querySelector('meta[name="csrf-header"]').content;
  const csrfToken = document.querySelector('meta[name="csrf-token"]').content;

  try {
    const response = await fetch(path, {
      method: "DELETE",
      body: new FormData(),
      headers: {
        [csrfHeader]: csrfToken
      }
    });

    if (!response.ok) {
      const text = await response.text();

      let message;
      try {
        message = JSON.parse(text);
      } catch (e) {
        console.log("Not JSON error body");
        return;
      }

      if (message.warning) {
        showWarning(message.warning);
      }

      return;
    }

    // Success
    location.reload(true);

  } catch (error) {
    console.error("Network error:", error);
  }
}

async function sendPostRequest(path) {

  const csrfHeader = document.querySelector('meta[name="csrf-header"]').content;
  const csrfToken = document.querySelector('meta[name="csrf-token"]').content;

  try {
    const response = await fetch(path, {
      method: "POST",
      body: new FormData(),
      headers: {
        [csrfHeader]: csrfToken
      }
    });

    const text = await response.text();

    let message;
    try {
      message = JSON.parse(text);
    } catch (e) {
      // console.log("Not JSON error body");
      // return;
    }

    if (!response.ok) {

      if (message && message.warning) {
        showWarning(message.warning);
      }
      return;
    }

    // Success
    if (message) {
      if (message.order_id) {
        location.replace("/checkout/" + message.order_id);
      }

      return;
    }

    location.reload(true);

  } catch (error) {
    console.error("Network error:", error);
  }
}
