// ================SIRENS SECTION================
//SIREN________________________________
/*
Res = Response på linje 7
'${siren.status}' er fordi det er en String tekst. Javascript kræver det.

*/
function loadSirens() {
    fetch("/sirens")
        .then(res => res.json())
        .then(data => {
            const tbody = document.getElementById("sirens-table-body");
            tbody.innerHTML = "";

            data.forEach(siren => {
                const row = document.createElement("tr");
                row.innerHTML = `
                    <td>${siren.id}</td>
                    <td>${siren.latitude}</td>
                    <td>${siren.longitude}</td>
                    <td>${siren.status}</td>
                    <td>${siren.disabled}</td>
                    <td>
                        <button onclick="editSiren(${siren.id}, ${siren.latitude}, ${siren.longitude}, '${siren.status}', ${siren.disabled})">Rediger</button>
                        <button onclick="deleteSiren(${siren.id})">Slet</button>
                    </td>
                `;
                tbody.appendChild(row);
            });
        });
}

//SIREN OPRET/REDIGER__________________
/*
Hvis man indsender en <form> så reloader Browseren siden som standard
DET vil vi undgå, så preventDefault() forhindrer det, så vi kan håndtere alt uden page reload.
*/
function submitSiren(event) {
    event.preventDefault();

    const id = document.getElementById("siren-id").value;
    const body = {
        latitude: parseFloat(document.getElementById("siren-lat").value),
        longitude: parseFloat(document.getElementById("siren-lon").value),
        status: document.getElementById("siren-status").value,
        disabled: document.getElementById("siren-disabled").value.toLowerCase() === "true"
    };

    /*
    method og url variables:
        Hvis der er id i feltet:
            - Vi vil opdatere -> PUT /sirens/{id}
        Hvis id er tomt:
            - Vi vil oprette -> POST /sirens
        Så vi vælger enten PUT med /sirens/${id}, eller POST med /sirens.
     */
    const method = id ? "PUT" : "POST";
    const url = id ? `/sirens/${id}` : "/sirens";

    fetch(url, {
        method: method,
        headers: {"Content-Type": "application/json" },
        body: JSON.stringify(body)
    })
        .then(() => {
            clearSirenForm();
            loadSirens();
            closeSirenForm();
        });
}
// Modal
function openSirenForm() {
    clearSirenForm();
    document.getElementById("siren-modal").style.display = "block";
}

function closeSirenForm() {
    document.getElementById("siren-modal").style.display = "none";
}

// Hvis man klikker udenfor modal, luk den
window.onclick = function(event) {
    const modal = document.getElementById("siren-modal");
    if (event.target === modal) {
        modal.style.display = "none";
    }
}

// Redigere en Siren
function editSiren(id, lat, lon, status, disabled)  {
    document.getElementById("siren-id").value = id;
    document.getElementById("siren-lat").value = lat;
    document.getElementById("siren-lon").value = lon;
    document.getElementById("siren-status").value = status;
    document.getElementById("siren-disabled").value = disabled;
    document.getElementById("siren-modal").style.display = "block";
}

// Clear form når man submit en siren, så kan man skrive en ny igen.
function clearSirenForm()  {
    document.getElementById("siren-id").value = "";
    document.getElementById("siren-lat").value = "";
    document.getElementById("siren-lon").value = "";
    document.getElementById("siren-status").value = "FRED";
    document.getElementById("siren-disabled").value = "false";
}

//SIREN SLET___________________________
function deleteSiren(id) {
    fetch(`/sirens/${id}`, { method: "DELETE"})
        .then(() => loadSirens());
}
//===============================================

//=================SHOW SECTION==================
function showSection(name) {
    // Skjul alle sektioner først
    document.getElementById("sirens-section").style.display="none";
    document.getElementById("fires-section").style.display="none";
    document.getElementById("simulate-section").style.display="none";

    // Vis den ønskede sektion
    document.getElementById(`${name}-section`).style.display = "block";

    // Hent data når det er relevant
    if (name === "sirens") loadSirens();
    if (name === "fires") loadFires();
}
loadSirens();
//===============================================

//=================FIRE SECTION==================
function loadFires() {
    fetch("/fires?status=active")
        .then(res => res.json())
        .then(data => {
            const tbody = document.getElementById("fires-table-body");
            tbody.innerHTML = "";

            data.forEach(fire => {
                const row = document.createElement("tr");
                const time = fire.time ? fire.time.replace("T", " ").substring(0, 19) : "Ukendt";
                //Ovenstående kode undgå at få fejl når time er null.

                row.innerHTML = `
                    <td>${fire.id}</td>
                    <td>${fire.latitude}</td>
                    <td>${fire.longitude}</td>
                    <td>${time}</td>
                    <td>
                        <button onclick="closeFire(${fire.id})">Luk</button>
                    </td>
                `;
                tbody.appendChild(row);
            });
        });
}

function closeFire(id) {
    fetch(`/fires/${id}/closure`, {
        method: "PUT"
    }).then(() => loadFires());
}

function openDeleteModal() {
    document.getElementById("delete-modal").style.display = "block";
}

function closeDeleteModal() {
    document.getElementById("delete-modal").style.display = "none";
}

function deleteFiresBeforeDate(event) {
    event.preventDefault();

    const date = document.getElementById("delete-date").value;

    fetch(`/fires/before-date?date=${date}`, {
        method: "DELETE"
    })
        .then(() => {
            document.getElementById("delete-date-result").textContent =
                `Alle brande før og med ${date} er slettet.`;
            closeDeleteModal();
            loadFires();
        })
        .catch(err => {
            console.error("Fejl ved sletning:", err);
            document.getElementById("delete-date-result").textContent =
                "Noget gik galt ved sletning.";
        });
}
// Luk modal ved klik udenfor
window.addEventListener("click", function(event) {
    const modal = document.getElementById("delete-modal");
    if (event.target === modal) {
        closeDeleteModal();
    }
});
//===============================================

//==============SIMULATION SECTION===============
function createFire(event) {
    event.preventDefault();

    const lat = parseFloat(document.getElementById("fire-lat").value);
    const lon = parseFloat(document.getElementById("fire-lon").value);

    fetch(`/fires?latitude=${lat}&longitude=${lon}`, {
        method: "POST"
    })
        .then(res => res.json())
        .then(data => {
            const sirenCount = data.sirens ? data.sirens.length: 0;
            document.getElementById("fire-result").textContent =
                `Brand oprettet ved (${lat}, ${lon}) - ${sirenCount} sirener aktiveret.`;

            // Ryd felterne
            document.getElementById("fire-lat").value = "";
            document.getElementById("fire-lon").value = "";
        })
        .catch(error => {
            console.error("Fejl: ", error);
            document.getElementById("fire-result").textContent = "Noget gik galt.";
        });
}
//===============================================