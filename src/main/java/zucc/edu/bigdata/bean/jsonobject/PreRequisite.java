package zucc.edu.bigdata.bean.jsonobject;


/**
 * prerequisite.json
 */
public class PreRequisite {
    private String concept_A; //先修概念
    private String concept_B; //后修概念

    public String getConcept_A() {
        return concept_A;
    }

    public void setConcept_A(String concept_A) {
        this.concept_A = concept_A;
    }

    public String getConcept_B() {
        return concept_B;
    }

    public void setConcept_B(String concept_B) {
        this.concept_B = concept_B;
    }

    @Override
    public String toString() {
        return "PreRequisite{" +
                "concept_A='" + concept_A + '\'' +
                ", concept_B='" + concept_B + '\'' +
                '}';
    }

}
