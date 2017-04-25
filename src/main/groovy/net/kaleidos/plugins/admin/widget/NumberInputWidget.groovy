package net.kaleidos.plugins.admin.widget

class NumberInputWidget extends InputWidget {
    NumberInputWidget() {
        inputType = "number"
    }

    @Override
    public void updateValue() {
        if (value) {
            Object.updateValue(parse("$value"))
        }
    }

    def parse(String str) {
        def number = null;

        try {
            number = Integer.parseInt(str);
        } catch(NumberFormatException e3) {
            try {
                number = Long.parseLong(str);
            } catch(NumberFormatException e2) {
                throw e3;
            }
        }
        return number;
    }
}
