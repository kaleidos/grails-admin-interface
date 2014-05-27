package net.kaleidos.plugins.admin.widget

class NumberInputWidget extends InputWidget{

    NumberInputWidget() {
        inputType = "number"
    }

    @Override
    def getValueForJson() {
        return value
    }

    @Override
    public void updateValue() {
        if (value) {
            super.updateValue(parse("$value"))
        }
    }

    def parse(String str) {
        def number = null;
        try {
            number = Long.parseLong(str);
        } catch(NumberFormatException e2) {
            try {
                number = Integer.parseInt(str);
            } catch(NumberFormatException e3) {
                throw e3;
            }
        }
        return number;
    }
}
