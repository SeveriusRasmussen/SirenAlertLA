// =====SIREN VISNING=====

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

// =====SIREN OPRET/REDIGER=====

function submitSiren(event) {
    event.preventDefault();

    const id = document.getElementById("siren-id").value;
    const body = {
        latitude: parseFloat(document.getElementById("siren-lat").value),
        longitude: parseFloat(document.getElementById("siren-lon").value),
        status: document.getElementById("siren-status").value,
        disabled: document.getElementById("siren-disabled").value === true
    }
}

loadSirens();
