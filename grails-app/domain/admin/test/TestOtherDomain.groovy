package admin.test

class TestOtherDomain {
    String name
    Integer year

    static belongsTo = [domain:TestDomain]

    static constraints = {
        name nullable:false
        year nullable:true
    }
}
