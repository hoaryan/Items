function showMessage(){
    Effect.Appear('messagebox', {duration: 0.3});
    setTimeout('appearMessage()', 800);
    var width = window.innerWidth-40;8
    document.getElementById('messagebox').style.width=width+'px';
}

function appearMessage(){
    Effect.Fade('messagebox', {duration: 0.3});
}

//function showAdditionaltoolarea(){
////    Effect.SlideDown('additionaltoolarea', { duration: 0.2, queue: { position: 'end', scope: 'menuscope', limit: 1, afterFinish:document.wrapperForm.searchInput.focus()} } );
////    alert(Effect.Queue);
//    // effekt ausgeschaltet, da sonst der focus auf das search field nicht funktioniert
//    document.getElementById('additionaltoolarea').style.display='block';
//    resetSearchField();
//}

//function hideAdditionaltoolarea(){
//    resetSearchField();
//    document.getElementById('additionaltoolarea').style.display='none';
//     // effekt ausgeschaltet, da sonst der reset des search fields nicht funktioniert
//    // Effect.SlideUp('additionaltoolarea', { duration: 0.2, queue:  { position: 'end', scope: 'menuscope', limit: 1} } );
////    alert(Effect.Queue);
//}

// var isSearchToolbarOpen = 0;

//function toggleAdditionaltoolarea(){
//  if(isSearchToolbarOpen == 1){
//    hideAdditionaltoolarea();
//    isSearchToolbarOpen = 0;
//  } else {
//    showAdditionaltoolarea();
//    isSearchToolbarOpen = 1;
//  }
//}


function toggleTimeactive(){

      var isTimeactive = $('timeactive').checked == true;

      $('starttime_hour').disabled = !isTimeactive;
      $('starttime_minute').disabled = !isTimeactive;

      if($('zeitspanne').checked == true){
        $('endtime_hour').disabled = !isTimeactive;
        $('endtime_minute').disabled = !isTimeactive;
      } else {
        $('endtime_hour').disabled = true;
        $('endtime_minute').disabled = true;
      }
            
}

function toggleZeitspanne(){
    var isZeitspanne = $('zeitspanne').checked == true;
//    alert('isZeitspanne: ' + isZeitspanne)
    var col = isZeitspanne? "#FFF" : "#CCC";
    $('divZeitspanne').style.backgroundColor = col;    
    $('endDate_value').disabled = !isZeitspanne;
    if($('timeactive').checked == false){
        $('endtime_hour').disabled = true;
        $('endtime_minute').disabled = true;
    } else {
        $('endtime_hour').disabled = !isZeitspanne;
        $('endtime_minute').disabled = !isZeitspanne;
    }
    

    
}

var isDateActive = false;
function toggleDate(){
    document.getElementById('dateArea').style.display = isDateActive? "none" : "inline";
    document.getElementById('toggleDateLink').innerHTML = isDateActive?"add Date":"hide Date";
    document.getElementById('existDate').value = isDateActive? "false" : "true";
    isDateActive = !isDateActive ;
}

//clearDates(){
//    $('starttime_hour').value
//}

    var yesHandler = function(o) {
        alert('You clicked "Yes"');
        this.cancel();
    }



function resetSearchField() {
        document.wrapperForm.reset()
        document.wrapperForm.searchInput.focus()
        document.wrapperForm.searchInput.onkeyup()
    }

function focusSearchFieldonKeyPressed(key) {
//    alert('keyCode: KAPLAN :/')
    if (!evt){
        evt = window.event;
    }

     document.wrapperForm.searchInput.focus();

//    var altPressed = evt.altKey;
//    var aPressed = evt.keyCode==65;
//
//    if(altPressed && aPressed){// ALT+a
//            $('email').value="horst.sueggel@googlemail.com";
//            $('password').value="test";
//        }

//    if (!evt){
//        evt = window.event;
//    }
        
//    alert("evt.keyCode");

//        var numberOrLetterPressed = (evt.keyCode>=48) && (evt.keyCode<=90);
//
//        alert('numberOrLetter: ' + numberOrLetterPressed + "  \ncode: " + evt.keyCode)
//        if(numberOrLetterPressed){// Buchstabe oder Zahl eingetippt
//            $('wrapperForm').searchInput.focus()
//            document.wrapperForm.searchInput.focus();
//        }

    }

function showSpinner(visible) {
    $('spinner').style.display = visible? "inline" : "none";
}

function showSpinner_2(visible, tag ) {
        var el = 'spinner_' + tag;
//        alert(el);
        document.getElementById(el).style.display = visible? "inline" : "none";
    }

function openCreate(){

    window.releaseEvents(Event.KEYPRESS);
    window.onkeydown = null;
    document.getElementById('createArea').style.display = "inline";
    document.createForm.name.focus();
    

//    window.captureEvents(Event.KEYPRESS);
//    window.onkeydown = focusSearchFieldonKeyPressed;
}

function closeCreate(){


    document.getElementById('createArea').style.display = "none";
    document.getElementById('name').value="";
    document.getElementById('name').style.borderWidth ="1px";
    document.getElementById('name').style.borderColor ="#00F";
    document.getElementById('name').style.backgroundColor ="#FFF";

    document.getElementById('notes').value="";

    document.getElementById('errormessage').style.display = "none";
    



    window.captureEvents(Event.KEYPRESS);
    window.onkeydown = focusSearchFieldonKeyPressed;
}