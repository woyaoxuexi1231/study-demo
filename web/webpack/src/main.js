require('./main.css');
require('./empty.css')
// 加载logo.png文件
const logo = require('./logo.png');


document.querySelector('#btn').addEventListener('click', function(){
    alert('Hello World');
}, false);
document.querySelector('#logo').src = logo;

