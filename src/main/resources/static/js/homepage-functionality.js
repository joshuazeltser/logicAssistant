/**
 * Created by joshuazeltser on 24/05/2017.
 */

$(function () {
    $('[data-toggle="tooltip"]').tooltip()

});

$(function() {
    $(".lined").linedtextarea(
        {selectedLine: 1}
    );
});

function readBlob(opt_startByte, opt_stopByte) {

    var files = document.getElementById('files').files;
    if (!files.length) {
        alert('Please select a file!');
        return;
    }

    var file = files[0];
    var start = parseInt(opt_startByte) || 0;
    var stop = parseInt(opt_stopByte) || file.size - 1;

    var reader = new FileReader();

    // If we use onloadend, we need to check the readyState.
    reader.onload = function(evt) {
        if (evt.target.readyState == FileReader.DONE) { // DONE == 2

            var mytextbox1 = document.getElementById('Text1');
            var mytextbox2 = document.getElementById('Text2');


            var temp = evt.target.result;
            var lines = temp.toString().split(/[\r\n]+/g);

            var i = 0;
            while (i != lines.length) {
                var components = lines[i].split(' , ');
                mytextbox1.value = mytextbox1.value + components[0] + '\n';

                // alert(components[1].indexOf("A"));
                if (components[1].indexOf("Available") != -1) {
                    mytextbox2.value = mytextbox2.value + convertEntities("&#10004;") + "-" + components[1] + '\n';
                    i++;
                    continue;
                }

                var rule = components[1].split('-');

                switch(rule[0]) {
                    case "And": mytextbox2.value = mytextbox2.value + convertEntities('&and;') + "-" + rule[1] + '\n'; break;
                    case "Or": mytextbox2.value = mytextbox2.value + convertEntities("&or;") + "-" + rule[1] + '\n'; break;
                    case "Implies": mytextbox2.value = mytextbox2.value + convertEntities('&rarr;') + "-" + rule[1] + '\n'; break;
                    case "Only": mytextbox2.value = mytextbox2.value + convertEntities('&harr;') + "-" + rule[1] + '\n'; break;
                    case "Not": mytextbox2.value = mytextbox2.value + convertEntities('&not;') + "-" + rule[1] + '\n'; break;
                    case "DoubleNot": mytextbox2.value = mytextbox2.value + convertEntities("&not;&not;") + "-" + rule[1] + '\n'; break;
                    case "Available": mytextbox2.value = mytextbox2.value + convertEntities("&#10004;") + "-" + rule[1] + '\n'; break;
                    default: mytextbox2.value = mytextbox2.value + components[1] + '\n';
                }

                i++;
            }
            for (var k = 0; k < 10; k++) {
                formatHTML();
            }
        }
    };
    var blob = file.slice(start, stop + 1);
    reader.readAsBinaryString(blob);
}

function convertEntities(html) {
    var el = document.createElement("div");
    el.innerHTML = html;
    return el.firstChild.data;
}

function decodeEntities(s){
    var str, temp= document.createElement('p');
    temp.innerHTML= s;
    str= temp.textContent || temp.innerText;
    temp=null;
    return str;
}

$(function () {
    document.querySelector('.readBytesButtons').addEventListener('click', function (evt) {
        if (evt.target.tagName.toLowerCase() == 'button') {
            var startByte = evt.target.getAttribute('data-startbyte');
            var endByte = evt.target.getAttribute('data-endbyte');
            readBlob(startByte, endByte);
        }

    }, false);
});

$(".lead").tooltip({
    placement: "top"
});






function indent_function() {
    var proof = document.getElementById('Text1');
    var rules = document.getElementById('Text2');

    var proofArray = proof.value.split("\n");
    var rulesArray = rules.value.split("\n");

    proof.value = "";
    
    var indentArray = new Array();

    /*<![CDATA[*/
    for (var i = 0; i < proofArray.length; i++) {
        if (i > 0) {
            proof.value = proof.value + '\n';
        }

        if (i >= rulesArray.length) {
            for (var j = i; j < proofArray.length; j++) {
                proof.value = proof.value + proofArray[j] + '\n';
            }
            break;
        }

        if (rulesArray[i] === "ASSUMPTION") {
            indentArray.push(1)
        }

        if (rulesArray[i].indexOf(decodeEntities('&rarr;') + "-Intro") >= 0
            || rulesArray[i].indexOf(decodeEntities('&or;') + "-Elim") >= 0) {
            indentArray.pop();
        }


        if (indentArray.length > 0) {
            var added = false;
            for (var j = 0; j < indentArray.length; j++) {
                if (proofArray[i].charAt(j) == '-') {
                    continue;
                }
                if (proofArray[i].charAt(j) != ' ') {
                    if (rulesArray[i] != '' && rulesArray[i].indexOf(decodeEntities('&or;') + "-Elim") < 0) {
                        proof.value = proof.value + '----';
                        added = true;
                    }
                }
            }
            if (added) {
                proof.value = proof.value + '  ' + proofArray[i];
            } else {
                proof.value = proof.value + proofArray[i];
            }
            if (rulesArray[i].indexOf(decodeEntities('&not;') + "-Elim") >= 0) {
                indentArray.pop();
            }
        } else {
            proof.value = proof.value + proofArray[i];
        }

    }

    /*]]>*/
}




$(function () {
    var textFile = null,
        makeTextFile = function (text) {
            var data = new Blob([text], {type: 'text/plain'});

            // If we are replacing a previously generated file we need to
            // manually revoke the object URL to avoid memory leaks.
            if (textFile !== null) {
                window.URL.revokeObjectURL(textFile);
            }

            textFile = window.URL.createObjectURL(data);

            return textFile;
        };

    var saveButton = document.getElementById('save');
    saveButton.onclick = function () {
        var proof = document.getElementById('Text1');
        var rules = document.getElementById('Text2');

        var proofArray = proof.value.split("\n");
        var rulesArray = rules.value.split("\n");
        var output = "";



        /*<![CDATA[*/
        for (var i = 0; i < proofArray.length; i++) {

                var comps = proofArray[i].split(' ');

                for (var j = 0; j < comps.length; j++) {

                    switch (comps[j]) {
                        case decodeEntities('&and;'):
                            output = output + '^ ';
                            break;
                        case decodeEntities('&or;'):
                            output = output + '| ';
                            break;
                        case decodeEntities('&rarr;'):
                            output = output + '-> ';
                            break;
                        case decodeEntities('&harr;'):
                            output = output + '<-> ';
                            break;

                        default:
                        if (comps[j].indexOf(decodeEntities('&not;&not;')) >= 0) {
                            output = output + '!!';
                            output = output + comps[j].substring(1,comps[j].length) + ' ';
                        } else if (comps[j].indexOf(decodeEntities('&not;')) >= 0) {
                            output = output + '!';
                            output = output + comps[j].substring(1,comps[j].length) + ' ';
                        } else {
                            output = output + comps[j] + ' ';
                        }

                    }

                }
            output = output + ', ';
            var comps2 = rulesArray[i].split('-');

            for (var j = 0; j < comps2.length; j++) {

                switch (comps2[j]) {
                    case decodeEntities('&and;'):
                        output = output + 'And-';
                        break;
                    case decodeEntities('&or;'):
                        output = output + 'Or-';
                        break;
                    case decodeEntities('&rarr;'):
                        output = output + 'Implies-';
                        break;
                    case decodeEntities('&harr;'):
                        output = output + 'Only-';
                        break;
                    case decodeEntities('&not;&not;'):
                        output = output + 'NotNot-';
                        break;
                    case decodeEntities('&not;'):
                        output = output + 'Not-';
                        break;
                    case decodeEntities('&#10004;'):
                        output = output + '';
                        break;

                    default:
                        output = output + comps2[j];
                }

            }


            if (i < proofArray.length - 1) {
                output = output + '\n';
            }

        }
//                        alert(output);
        var link = document.getElementById('downloadlink');
        link.href = makeTextFile(output);
        link.click();
        /*]]>*/
    }

});
$(function () {
    window.onload = hideHelper;
});

function changeColour() {
    var colour = new Array();
    var hint = document.getElementById('hintButton');
    if (hint.getAttribute('class') == 'btn btn-primary') {
        hint.setAttribute('class', 'btn btn-warning');
        colour.push('btn btn-warning');

    } else if (hint.getAttribute('class') == 'btn btn-warning') {
        hint.setAttribute('class', 'btn btn-success');
        colour.push('btn btn-success');
    } else {
        hint.setAttribute('class', 'btn btn-primary');
        colour.push('btn btn-primary');
    }
    localStorage.setItem('colour', JSON.stringify(colour));
}

function hideHelper() {

    console.log('hello');
    var list = JSON.parse(localStorage.getItem('colour'));
    var hint = document.getElementById('hintButton');

    // console.log(list.pop());

    var c = list.pop();

    if (c === 'btn btn-warning') {
        hint.setAttribute('class', 'btn btn-warning');
        console.log(hint.getAttribute('class'));
        $("#hintBox").show();
        $("#errorBox").hide();
        $("#aHintBox").hide();
    }

    if (c === 'btn btn-success') {
        hint.setAttribute('class', 'btn btn-success');
        console.log(hint.getAttribute('class'));
        $("#hintBox").hide();
        $("#errorBox").hide();
        $("#aHintBox").show();
    }

    if (c === 'btn btn-primary') {
        hint.setAttribute('class', 'btn btn-primary');
        console.log(hint.getAttribute('class'));
        $("#hintBox").hide();
        $("#errorBox").show();
        $("#aHintBox").hide();
    }

    checkHintStatus();
//
}



function postSubmit() {
//                                e.preventDefault();
//                                this.submit();

    setTimeout(hideHelper()
        , 100);
}

$(function () {
    var mytextbox2 = document.getElementById('Text2');
    var mybutton1 = document.getElementById('b1');
    var mybutton2 = document.getElementById('b2');
    var mybutton3 = document.getElementById('b3');
    var mybutton4 = document.getElementById('b4');
    var mybutton5 = document.getElementById('b5');
    var mybutton6 = document.getElementById('b6');
    var mybutton7 = document.getElementById('b7');
    var mybutton8 = document.getElementById('b8');
    var mybutton9 = document.getElementById('b9');
    var mybutton10 = document.getElementById('b10');
    var mybutton11 = document.getElementById('b11');
    var mybutton12 = document.getElementById('b12');
    var mybutton13 = document.getElementById('b13');
    var mybutton15 = document.getElementById('b15');

    mybutton1.onclick = function () {
        mytextbox2.value = mytextbox2.value + this.value + '\n';

    }
    mybutton2.onclick = function () {
        mytextbox2.value = mytextbox2.value + this.value + '\n';

    }
    mybutton3.onclick = function () {
        mytextbox2.value = mytextbox2.value + this.value + '\n';

    }
    mybutton4.onclick = function () {
        mytextbox2.value = mytextbox2.value + this.value + '\n';

    }
    mybutton5.onclick = function () {
        mytextbox2.value = mytextbox2.value + this.value + '\n';

    }
    mybutton6.onclick = function () {
        mytextbox2.value = mytextbox2.value + this.value + '\n';

    }
    mybutton7.onclick = function () {
        mytextbox2.value = mytextbox2.value + this.value + '\n';

    }
    mybutton8.onclick = function () {
        mytextbox2.value = mytextbox2.value + this.value + '\n';

    }
    mybutton9.onclick = function () {
        mytextbox2.value = mytextbox2.value + this.value + '\n';

    }
    mybutton10.onclick = function () {
        mytextbox2.value = mytextbox2.value + this.value + '\n';

    }
    mybutton11.onclick = function () {
        mytextbox2.value = mytextbox2.value + this.value + '\n';

    }
    mybutton12.onclick = function () {
        mytextbox2.value = mytextbox2.value + this.value + '\n';

    }
    mybutton13.onclick = function () {
        mytextbox2.value = mytextbox2.value + this.value + '\n';

    }

    var mybutton14 = document.getElementById('b14');
    mybutton14.onclick = function () {
        $("#Text2").click();
    };

    mybutton15.onclick = function () {
        mytextbox2.value = mytextbox2.value + this.value + '\n';

    }
});

