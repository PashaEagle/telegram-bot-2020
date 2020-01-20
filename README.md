# telegram-bot

## Keyboard

An example of keyboard json configuration:
```json
{
   "name":"keyboardName",
   "text":{
      "key":"messageText",
      "argGenerationMethodPath":"package.class.methodName"
   },
   "keyboard":[
      [
         {
            "label":{
               "key":"labelText",
               "argGenerationMethodPath":"package.class.methodName"
            },
            "action":"package.class.methodName",
            "callbackData":"buttonID",
            "isVisible":"true/false",
            "nextKeyboard":"nextKeyboardName",
            "inputDataValidationMethodPath":"package.class.methodName"
         }
      ]
   ]
}
```
**Where:**
* **"name"** - name of keyboard,
* **"text.key"** - text we want to sent to user,
* **"text.argGenerationMethod"** - path of method that returns text properties,
* **"label.key"** - text of the button,
* **"label.argGenerationMethod"** - path of method that returns button properties,
* **"action"** - path of action method of the button,
* **"callbackData"** - pagination id of the button,
* **"nextKeyboard"** - name of next keyboard,
* **"inputDataValidationMethodPath"** - path of method that validates user input.