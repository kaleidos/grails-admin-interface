package admin.test

class TestDomain {
    transient transientTest
    String name
    int year

    static hasMany=[domain:TestDomain]
    static hasOne=[otherDomain:TestOtherDomain]

    static constraints = {
        name nullable:false
        year nullable:true
        domain nullable:true
        otherDomain nullable:true
    }

    String toString(){
        return name
    }
}
