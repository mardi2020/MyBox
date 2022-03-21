import axios from 'axios';

const client = new axios.create();

// client.defaults.baseUrl = 'http://192.168.0.26'
client.defaults.withCredentials = true
// client.defaults.headers.common['Authorization'] = 'Bearer a1b2c3d4';
//
// axios.intercepter.response.use(response=> {
//     return response;
// },
//     error=> {
//     return Promise.reject(error);
//     })


export default client;