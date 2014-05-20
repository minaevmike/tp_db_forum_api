function createUser(message) 
{
    var client = new XMLHttpRequest();
    client.open("POST", "/db/api/user/create/");
    client.setRequestHeader("Content-Type", "text/plain;charset=UTF-8");
    client.send(message);
}

var user = {
    username    : "user",
    about       : "hello im user",
    isAnonymous : false,
    name        : "username",
    email       : "exam2135ple@mail.ru"
};

function loadTest(inserts) 
{
    for(var i = 0; i < inserts; i++) {
       user.username    = "user" + i;
       user.about       = "hello im user" + i;
       user.isAnonymous = false;
       user.name        = "username" + i;
       user.email       = "exam2135ple@mail.ru" + i;
        
       createUser(JSON.stringify(user));
    }
}

loadTest(10E5);
