// # ENVIRONMENT #
const API_URL = "http://localhost:8080/";

function toPascalCase(string) {
  return string
    .replace(/([a-z])([A-Z])/g, '$1 $2')
    .replace(/[^a-zA-Z0-9]+/g, ' ')
    .split(' ')
    .map(word => word.charAt(0).toUpperCase() + word.slice(1).toLowerCase())
    .join('');
}

const checkUser = async () => {
  try {
    const response = await fetch(API_URL , {
        method : 'GET',
        credentials: 'include'
    });

    const res = await response.text();

    if (!res.includes("Guest")) {
        window.location.href = "/";
    } else {
        document.body.style.display = "block";
    }

  } catch (error) {
    console.log(error);
  }
}

const send = (queryParam) => {
window.location.href = API_URL + `login/oauth2/authorization/app?client=${queryParam}`
};
