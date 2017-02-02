function add(type) {

    //Create an input type dynamically.
    var element = document.createElement("input");

    //Assign different attributes to the element.
    element.setAttribute("type", type);
    element.setAttribute("value", type);
    element.setAttribute("name", type);


    var foo = document.getElementById("fooBar");

    //Append the element in page (in span).
    foo.appendChild(element);
}