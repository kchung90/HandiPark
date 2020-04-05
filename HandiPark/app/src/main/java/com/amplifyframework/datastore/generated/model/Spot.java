package com.amplifyframework.datastore.generated.model;


import java.util.List;
import java.util.UUID;
import java.util.Objects;

import androidx.core.util.ObjectsCompat;

import com.amplifyframework.core.model.Model;
import com.amplifyframework.core.model.annotations.Index;
import com.amplifyframework.core.model.annotations.ModelConfig;
import com.amplifyframework.core.model.annotations.ModelField;
import com.amplifyframework.core.model.query.predicate.QueryField;

import static com.amplifyframework.core.model.query.predicate.QueryField.field;

/** This is an auto generated class representing the Spot type in your schema. */
@SuppressWarnings("all")
@ModelConfig(pluralName = "Spots")
public final class Spot implements Model {
  public static final QueryField ID = field("id");
  public static final QueryField ADDRESS = field("address");
  public static final QueryField SPACES = field("spaces");
  public static final QueryField COORDINATES = field("coordinates");
  public static final QueryField NOTES = field("notes");
  private final @ModelField(targetType="ID", isRequired = true) String id;
  private final @ModelField(targetType="String", isRequired = true) String address;
  private final @ModelField(targetType="Int", isRequired = true) Integer spaces;
  private final @ModelField(targetType="Float", isRequired = true) List<Float> coordinates;
  private final @ModelField(targetType="String") String notes;
  public String getId() {
      return id;
  }
  
  public String getAddress() {
      return address;
  }
  
  public Integer getSpaces() {
      return spaces;
  }
  
  public List<Float> getCoordinates() {
      return coordinates;
  }
  
  public String getNotes() {
      return notes;
  }
  
  private Spot(String id, String address, Integer spaces, List<Float> coordinates, String notes) {
    this.id = id;
    this.address = address;
    this.spaces = spaces;
    this.coordinates = coordinates;
    this.notes = notes;
  }
  
  @Override
   public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      } else if(obj == null || getClass() != obj.getClass()) {
        return false;
      } else {
      Spot spot = (Spot) obj;
      return ObjectsCompat.equals(getId(), spot.getId()) &&
              ObjectsCompat.equals(getAddress(), spot.getAddress()) &&
              ObjectsCompat.equals(getSpaces(), spot.getSpaces()) &&
              ObjectsCompat.equals(getCoordinates(), spot.getCoordinates()) &&
              ObjectsCompat.equals(getNotes(), spot.getNotes());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getId())
      .append(getAddress())
      .append(getSpaces())
      .append(getCoordinates())
      .append(getNotes())
      .toString()
      .hashCode();
  }
  
  public static AddressStep builder() {
      return new Builder();
  }
  
  /** 
   * WARNING: This method should not be used to build an instance of this object for a CREATE mutation.
   * This is a convenience method to return an instance of the object with only its ID populated
   * to be used in the context of a parameter in a delete mutation or referencing a foreign key
   * in a relationship.
   * @param id the id of the existing item this instance will represent
   * @return an instance of this model with only ID populated
   * @throws IllegalArgumentException Checks that ID is in the proper format
   **/
  public static Spot justId(String id) {
    try {
      UUID.fromString(id); // Check that ID is in the UUID format - if not an exception is thrown
    } catch (Exception exception) {
      throw new IllegalArgumentException(
              "Model IDs must be unique in the format of UUID. This method is for creating instances " +
              "of an existing object with only its ID field for sending as a mutation parameter. When " +
              "creating a new object, use the standard builder method and leave the ID field blank."
      );
    }
    return new Spot(
      id,
      null,
      null,
      null,
      null
    );
  }
  
  public CopyOfBuilder copyOfBuilder() {
    return new CopyOfBuilder(id,
      address,
      spaces,
      coordinates,
      notes);
  }
  public interface AddressStep {
    SpacesStep address(String address);
  }
  

  public interface SpacesStep {
    CoordinatesStep spaces(Integer spaces);
  }
  

  public interface CoordinatesStep {
    BuildStep coordinates(List<Float> coordinates);
  }
  

  public interface BuildStep {
    Spot build();
    BuildStep id(String id) throws IllegalArgumentException;
    BuildStep notes(String notes);
  }
  

  public static class Builder implements AddressStep, SpacesStep, CoordinatesStep, BuildStep {
    private String id;
    private String address;
    private Integer spaces;
    private List<Float> coordinates;
    private String notes;
    @Override
     public Spot build() {
        String id = this.id != null ? this.id : UUID.randomUUID().toString();
        
        return new Spot(
          id,
          address,
          spaces,
          coordinates,
          notes);
    }
    
    @Override
     public SpacesStep address(String address) {
        Objects.requireNonNull(address);
        this.address = address;
        return this;
    }
    
    @Override
     public CoordinatesStep spaces(Integer spaces) {
        Objects.requireNonNull(spaces);
        this.spaces = spaces;
        return this;
    }
    
    @Override
     public BuildStep coordinates(List<Float> coordinates) {
        Objects.requireNonNull(coordinates);
        this.coordinates = coordinates;
        return this;
    }
    
    @Override
     public BuildStep notes(String notes) {
        this.notes = notes;
        return this;
    }
    
    /** 
     * WARNING: Do not set ID when creating a new object. Leave this blank and one will be auto generated for you.
     * This should only be set when referring to an already existing object.
     * @param id id
     * @return Current Builder instance, for fluent method chaining
     * @throws IllegalArgumentException Checks that ID is in the proper format
     **/
    public BuildStep id(String id) throws IllegalArgumentException {
        this.id = id;
        
        try {
            UUID.fromString(id); // Check that ID is in the UUID format - if not an exception is thrown
        } catch (Exception exception) {
          throw new IllegalArgumentException("Model IDs must be unique in the format of UUID.",
                    exception);
        }
        
        return this;
    }
  }
  

  public final class CopyOfBuilder extends Builder {
    private CopyOfBuilder(String id, String address, Integer spaces, List<Float> coordinates, String notes) {
      super.id(id);
      super.address(address)
        .spaces(spaces)
        .coordinates(coordinates)
        .notes(notes);
    }
    
    @Override
     public CopyOfBuilder address(String address) {
      return (CopyOfBuilder) super.address(address);
    }
    
    @Override
     public CopyOfBuilder spaces(Integer spaces) {
      return (CopyOfBuilder) super.spaces(spaces);
    }
    
    @Override
     public CopyOfBuilder coordinates(List<Float> coordinates) {
      return (CopyOfBuilder) super.coordinates(coordinates);
    }
    
    @Override
     public CopyOfBuilder notes(String notes) {
      return (CopyOfBuilder) super.notes(notes);
    }
  }
  
}
