package admin.test

class TestDomain {
    String name
    Integer year

    static constraints = {
        name nullable:false
        year nullable:true
    }

    String toString(){
        return name
    }
}
