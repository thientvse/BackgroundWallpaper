package com.backendless.backgroundhd.data;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.geo.GeoPoint;
import com.backendless.persistence.BackendlessDataQuery;

public class Image_library
{
  private String type_image;
  private String id;
  private String name_type_image;
  private java.util.Date created;
  private String count;
  private java.util.Date updated;
  private String objectId;
  private String url_icon;
  private String ownerId;
  public String getType_image()
  {
    return type_image;
  }

  public void setType_image( String type_image )
  {
    this.type_image = type_image;
  }

  public String getId()
  {
    return id;
  }

  public void setId( String id )
  {
    this.id = id;
  }

  public String getName_type_image()
  {
    return name_type_image;
  }

  public void setName_type_image( String name_type_image )
  {
    this.name_type_image = name_type_image;
  }

  public java.util.Date getCreated()
  {
    return created;
  }

  public String getCount()
  {
    return count;
  }

  public void setCount( String count )
  {
    this.count = count;
  }

  public java.util.Date getUpdated()
  {
    return updated;
  }

  public String getObjectId()
  {
    return objectId;
  }

  public String getUrl_icon()
  {
    return url_icon;
  }

  public void setUrl_icon( String url_icon )
  {
    this.url_icon = url_icon;
  }

  public String getOwnerId()
  {
    return ownerId;
  }

                                                    
  public Image_library save()
  {
    return Backendless.Data.of( Image_library.class ).save( this );
  }

  public Future<Image_library> saveAsync()
  {
    if( Backendless.isAndroid() )
    {
      throw new UnsupportedOperationException( "Using this method is restricted in Android" );
    }
    else
    {
      Future<Image_library> future = new Future<Image_library>();
      Backendless.Data.of( Image_library.class ).save( this, future );

      return future;
    }
  }

  public void saveAsync( AsyncCallback<Image_library> callback )
  {
    Backendless.Data.of( Image_library.class ).save( this, callback );
  }

  public Long remove()
  {
    return Backendless.Data.of( Image_library.class ).remove( this );
  }

  public Future<Long> removeAsync()
  {
    if( Backendless.isAndroid() )
    {
      throw new UnsupportedOperationException( "Using this method is restricted in Android" );
    }
    else
    {
      Future<Long> future = new Future<Long>();
      Backendless.Data.of( Image_library.class ).remove( this, future );

      return future;
    }
  }

  public void removeAsync( AsyncCallback<Long> callback )
  {
    Backendless.Data.of( Image_library.class ).remove( this, callback );
  }

  public static Image_library findById( String id )
  {
    return Backendless.Data.of( Image_library.class ).findById( id );
  }

  public static Future<Image_library> findByIdAsync( String id )
  {
    if( Backendless.isAndroid() )
    {
      throw new UnsupportedOperationException( "Using this method is restricted in Android" );
    }
    else
    {
      Future<Image_library> future = new Future<Image_library>();
      Backendless.Data.of( Image_library.class ).findById( id, future );

      return future;
    }
  }

  public static void findByIdAsync( String id, AsyncCallback<Image_library> callback )
  {
    Backendless.Data.of( Image_library.class ).findById( id, callback );
  }

  public static Image_library findFirst()
  {
    return Backendless.Data.of( Image_library.class ).findFirst();
  }

  public static Future<Image_library> findFirstAsync()
  {
    if( Backendless.isAndroid() )
    {
      throw new UnsupportedOperationException( "Using this method is restricted in Android" );
    }
    else
    {
      Future<Image_library> future = new Future<Image_library>();
      Backendless.Data.of( Image_library.class ).findFirst( future );

      return future;
    }
  }

  public static void findFirstAsync( AsyncCallback<Image_library> callback )
  {
    Backendless.Data.of( Image_library.class ).findFirst( callback );
  }

  public static Image_library findLast()
  {
    return Backendless.Data.of( Image_library.class ).findLast();
  }

  public static Future<Image_library> findLastAsync()
  {
    if( Backendless.isAndroid() )
    {
      throw new UnsupportedOperationException( "Using this method is restricted in Android" );
    }
    else
    {
      Future<Image_library> future = new Future<Image_library>();
      Backendless.Data.of( Image_library.class ).findLast( future );

      return future;
    }
  }

  public static void findLastAsync( AsyncCallback<Image_library> callback )
  {
    Backendless.Data.of( Image_library.class ).findLast( callback );
  }

  public static BackendlessCollection<Image_library> find( BackendlessDataQuery query )
  {
    return Backendless.Data.of( Image_library.class ).find( query );
  }

  public static Future<BackendlessCollection<Image_library>> findAsync( BackendlessDataQuery query )
  {
    if( Backendless.isAndroid() )
    {
      throw new UnsupportedOperationException( "Using this method is restricted in Android" );
    }
    else
    {
      Future<BackendlessCollection<Image_library>> future = new Future<BackendlessCollection<Image_library>>();
      Backendless.Data.of( Image_library.class ).find( query, future );

      return future;
    }
  }

  public static void findAsync( BackendlessDataQuery query, AsyncCallback<BackendlessCollection<Image_library>> callback )
  {
    Backendless.Data.of( Image_library.class ).find( query, callback );
  }
}