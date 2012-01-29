function debugShortcuts(evt) {
        if (!evt){
            evt = window.event;
        }

        var altPressed = evt.altKey;
        var aPressed = evt.keyCode==65;

        if(altPressed && aPressed){// ALT+a
            $('email').value="horst.sueggel@googlemail.com";
            $('password').value="test";
        }
        
    }
