

function getJson(url) {
    var response;
    $.ajax({
        url: url,
        async: false,
        success: function (data) {
            response = data;
        }
    });

    return response;
}

function updateFieldsNO() {
    var referenceNmbNO = document.getElementById('refNO');
    var labelRefNO = document.getElementById('labelRefNO');
    var message = document.getElementById('message');
    var labelForMessage = document.getElementById('labelForMessage');

    if (referenceNmbNO.value !== "") {
        message.hidden = true;
        labelForMessage.hidden = true;
    } else if (message.value !== "") {
        referenceNmbNO.hidden = true;
        labelRefNO.hidden = true;
    } else {
        message.hidden = false;
        labelForMessage.hidden = false;
        referenceNmbNO.hidden = false;
        labelRefNO.hidden = false;
    }
}

function updateFieldsSE() {
    var message = document.getElementById('message');
    var labelForMessage = document.getElementById('labelForMessage');
    var labelRefSE = document.getElementById('labelRefSE');
    var referenceNmbSE = document.getElementById('refSE');
    var e = document.getElementById('to_account_type');

    if (e.options[e.selectedIndex].value === 'BGNR' || e.options[e.selectedIndex].value === 'PGNR') {
        if (referenceNmbSE.value !== "") {
            message.hidden = true;
            labelForMessage.hidden = true;
        } else if (message.value !== "") {
            referenceNmbSE.hidden = true;
            labelRefSE.hidden = true;
        } else {
            message.hidden = false;
            labelForMessage.hidden = false;
            referenceNmbSE.hidden = false;
            labelRefSE.hidden = false;
        }
    } else {
        referenceNmbSE.hidden = true;
        labelRefSE.hidden = true;
    }
}

function referenceOCRNO() {
    var referenceTypeNO = document.getElementById('referenceTypeNO');
    var referenceNmbNO = document.getElementById('refNO');

    if (referenceNmbNO.value !== "") {
        referenceTypeNO.value = 'OCR';
    }
}

function referenceOCRSE() {
    var referenceTypeSE = document.getElementById('referenceTypeSE');
    var referenceNmbSE = document.getElementById('refSE');

    if (referenceNmbSE.value !== "") {
        referenceTypeSE.value = 'OCR';
    }
}
