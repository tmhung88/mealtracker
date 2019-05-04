import 'whatwg-fetch'
import Promise from "bluebird";

const headers = {
    'Accept': 'application/json, text/plain, */*',
    'Content-Type': 'application/json'
}

function getFetch() {
    if ((window).customFetch) {
        return (window).customFetch;
    }

    return fetch;
}


function handleError(response) {


    return response;
}

function handleCatchError(error) {
    throw error;
}

export var get = function (path) {

    return Promise.resolve(getFetch()(path, {
        headers: headers,
        credentials: 'same-origin'
    })).then(handleError).catch(handleCatchError);
}

export var deleteRequest = function (path, data) {
    return Promise.resolve(getFetch()(path, {
        method: "DELETE",
        headers: headers,
        credentials: 'same-origin',
        body: stringifyContent(data)
    })).then(handleError).catch(handleCatchError)
}

export var put = function (path, data) {
    return Promise.resolve(getFetch()(path, {
        method: "PUT",
        headers: headers,
        credentials: 'same-origin',
        body: stringifyContent(data)
    })).then(handleError).catch(handleCatchError)
}

export var post = function (path, data) {
    return Promise.resolve(getFetch()(path, {
        method: "POST",
        headers: headers,
        credentials: 'same-origin',
        body: stringifyContent(data)
    })).then(handleError).catch(handleCatchError)
}

function stringifyContent(data) {
    if (!data) return null;
    if (typeof data == "string") {
        return data;
    }

    return JSON.stringify(data);
}