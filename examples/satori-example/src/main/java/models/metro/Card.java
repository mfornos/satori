package models.metro;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Card {
    private String id;
    private int laborForce;
    private String judicialBranch;
    private float inflationRate;
    private String externalDebt;
    private String country;
    private String disputes;
    private String constitution;
    private String militaryBranches;
    private String politicalParties;
    private String politicalPressureGroups;
    private String economy;
    private String internationalOrgPart;

    public Card(ResultSet rs) throws SQLException {
        from(rs);
    }

    public Card() {
    }

    public void from(ResultSet rs) throws SQLException {
        this.id = rs.getString("id");
        this.laborForce = rs.getInt("Labor force");
        this.inflationRate = rs.getFloat("Inflation rate (consumer prices)");
        this.externalDebt = rs.getString("Debt - external");
        this.politicalParties = rs.getString("Political parties and leaders");
        this.politicalPressureGroups = rs.getString("Political pressure groups and leaders");
        this.economy = rs.getString("Economy - overview");
        this.militaryBranches = rs.getString("Military branches");
        this.disputes = rs.getString("Disputes - international");
        this.constitution = rs.getString("Constitution");
        this.country = formatCountry(rs.getString("Country name"));
        this.internationalOrgPart = rs.getString("International organization participation");
    }

    private String formatCountry(String text) {
        if (text != null) {
            StringBuilder sb = new StringBuilder();
            String[] parts = text.split(" ");
            for (String p : parts) {
                if (p.matches("conventional|local|abbreviation:|former:|note:")) {
                    if(sb.length()>0) {
                        sb.append("</span>");
                    }
                    sb.append("<span class=\"sname\"><span class=\"label\">");
                }
                sb.append(p);
                sb.append(" ");
                if (p.endsWith(":")) {
                    sb.append("</span>");
                }
            }

            return sb.toString();
        }
        return null;
    }

    public String getDisputes() {
        return disputes;
    }

    public void setDisputes(String disputes) {
        this.disputes = disputes;
    }

    public String getConstitution() {
        return constitution;
    }

    public void setConstitution(String constitution) {
        this.constitution = constitution;
    }

    public String getMilitaryBranches() {
        return militaryBranches;
    }

    public void setMilitaryBranches(String militaryBranches) {
        this.militaryBranches = militaryBranches;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getExternalDebt() {
        return externalDebt;
    }

    public void setExternalDebt(String externalDebt) {
        this.externalDebt = externalDebt;
    }

    public int getLaborForce() {
        return laborForce;
    }

    public void setLaborForce(int laborForce) {
        this.laborForce = laborForce;
    }

    public String getJudicialBranch() {
        return judicialBranch;
    }

    public void setJudicialBranch(String judicialBranch) {
        this.judicialBranch = judicialBranch;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public float getInflationRate() {
        return inflationRate;
    }

    public void setInflationRate(float inflationRate) {
        this.inflationRate = inflationRate;
    }

    public String getPoliticalParties() {
        return politicalParties;
    }

    public void setPoliticalParties(String politicalParties) {
        this.politicalParties = politicalParties;
    }

    public String getPoliticalPressureGroups() {
        return politicalPressureGroups;
    }

    public void setPoliticalPressureGroups(String politicalPressureGroups) {
        this.politicalPressureGroups = politicalPressureGroups;
    }

    public String getEconomy() {
        return economy;
    }

    public void setEconomy(String economy) {
        this.economy = economy;
    }

    public String getInternationalOrgPart() {
        return internationalOrgPart;
    }

    public void setInternationalOrgPart(String internationalOrgPart) {
        this.internationalOrgPart = internationalOrgPart;
    }

}
