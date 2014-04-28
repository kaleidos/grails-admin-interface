package admin.test

class TestOtherDomain {
    String name
    Integer year

    static constraints = {
        name nullable:false
        year nullable:true
    }
}
