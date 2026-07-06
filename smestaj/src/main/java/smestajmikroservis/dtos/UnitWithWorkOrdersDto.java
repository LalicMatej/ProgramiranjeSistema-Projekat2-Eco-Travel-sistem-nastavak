// smestajmikroservis/dtos/UnitWithWorkOrdersDto.java
package smestajmikroservis.dtos;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "DTO za smeštajnu jedinicu sa radnim nalozima")
public class UnitWithWorkOrdersDto {

    @Schema(description = "ID smeštajne jedinice", example = "1")
    private Long id;

    @Schema(description = "Naziv smeštajne jedinice", example = "Apartman Centar")
    private String name;

    @Schema(description = "Tip smeštajne jedinice", example = "APARTMAN")
    private String unit_type;

    @Schema(description = "Cena po noćenju", example = "89.99")
    private Double basePricePerNight;

    @Schema(description = "Ulica i broj", example = "Knez Mihailova 25")
    private String streetAddress;

    @Schema(description = "Grad", example = "Beograd")
    private String city;

    @Schema(description = "Poštanski broj", example = "11000")
    private String zip_code;

    @Schema(description = "Lista radnih naloga za ovu jedinicu")
    private List<WorkOrderDto> workOrders;

    // Prazan konstruktor
    public UnitWithWorkOrdersDto() {}

    // Konstruktor koji prima UnitDto i listu radnih naloga
    public UnitWithWorkOrdersDto(UnitDto unit, List<WorkOrderDto> workOrders) {
        this.id = unit.getId();
        this.name = unit.getName();
        this.unit_type = unit.getUnit_type();
        this.basePricePerNight = unit.getBasePricePerNight();
        this.streetAddress = unit.getStreetAddress();
        this.city = unit.getCity();
        this.zip_code = unit.getZip_code();
        this.workOrders = workOrders;
    }

    // Getteri i setteri
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getUnit_type() { return unit_type; }
    public void setUnit_type(String unit_type) { this.unit_type = unit_type; }

    public Double getBasePricePerNight() { return basePricePerNight; }
    public void setBasePricePerNight(Double basePricePerNight) { this.basePricePerNight = basePricePerNight; }

    public String getStreetAddress() { return streetAddress; }
    public void setStreetAddress(String streetAddress) { this.streetAddress = streetAddress; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getZip_code() { return zip_code; }
    public void setZip_code(String zip_code) { this.zip_code = zip_code; }

    public List<WorkOrderDto> getWorkOrders() { return workOrders; }
    public void setWorkOrders(List<WorkOrderDto> workOrders) { this.workOrders = workOrders; }
}