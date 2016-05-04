package com.backendless.backgroundhd.data;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.geo.GeoPoint;
import com.backendless.persistence.BackendlessDataQuery;

public class Image_management
{
  private String objectId;
  private java.util.Date created;
  private String image_type;
  private String image_count_view;
  private String image_url_thumb;
  private String image_title;
  private java.util.Date updated;
  private String ownerId;
  private String image_id;
  public String getObjectId()
  {
    return objectId;
  }

  public java.util.Date getCreated()
  {
    return created;
  }

  public String getImage_type()
  {
    return image_type;
  }

  public void setImage_type( String image_type )
  {
    this.image_type = image_type;
  }

  public String getImage_count_view()
  {
    return image_count_view;
  }

  public void setImage_count_view( String image_count_view )
  {
    this.image_count_view = image_count_view;
  }

  public String getImage_url_thumb()
  {
    return image_url_thumb;
  }

  public void setImage_url_thumb( String image_url_thumb )
  {
    this.image_url_thumb = image_url_thumb;
  }

  public String getImage_title()
  {
    return image_title;
  }

  public void setImage_title( String image_title )
  {
    this.image_title = image_title;
  }

  public java.util.Date getUpdated()
  {
    return updated;
  }

  public String getOwnerId()
  {
    return ownerId;
  }

  public String getImage_id()
  {
    return image_id;
  }

  public void setImage_id( String image_id )
  {
    this.image_id = image_id;
  }

                                                    
  public Image_management save()
  {
    return Backendless.Data.of( Image_management.class ).save( this );
  }

  public Future<Image_management> saveAsync()
  {
    if( Backendless.isAndroid() )
    {
      throw new UnsupportedOperationException( "Using this method is restricted in Android" );
    }
    else
    {
      Future<Image_management> future = new Future<Image_management>();
      Backendless.Data.of( Image_management.class ).save( this, future );

      return future;
    }
  }

  public void saveAsync( AsyncCallback<Image_management> callback )
  {
    Backendless.Data.of( Image_management.class ).save( this, callback );
  }

  public Long remove()
  {
    return Backendless.Data.of( Image_management.class ).remove( this );
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
      Backendless.Data.of( Image_management.class ).remove( this, future );

      return future;
    }
  }

  public void removeAsync( AsyncCallback<Long> callback )
  {
    Backendless.Data.of( Image_management.class ).remove( this, callback );
  }

  public static Image_management findById( String id )
  {
    return Backendless.Data.of( Image_management.class ).findById( id );
  }

  public static Future<Image_management> findByIdAsync( String id )
  {
    if( Backendless.isAndroid() )
    {
      throw new UnsupportedOperationException( "Using this method is restricted in Android" );
    }
    else
    {
      Future<Image_management> future = new Future<Image_management>();
      Backendless.Data.of( Image_management.class ).findById( id, future );

      return future;
    }
  }

  public static void findByIdAsync( String id, AsyncCallback<Image_management> callback )
  {
    Backendless.Data.of( Image_management.class ).findById( id, callback );
  }

  public static Image_management findFirst()
  {
    return Backendless.Data.of( Image_management.class ).findFirst();
  }

  public static Future<Image_management> findFirstAsync()
  {
    if( Backendless.isAndroid() )
    {
      throw new UnsupportedOperationException( "Using this method is restricted in Android" );
    }
    else
    {
      Future<Image_management> future = new Future<Image_management>();
      Backendless.Data.of( Image_management.class ).findFirst( future );

      return future;
    }
  }

  public static void findFirstAsync( AsyncCallback<Image_management> callback )
  {
    Backendless.Data.of( Image_management.class ).findFirst( callback );
  }

  public static Image_management findLast()
  {
    return Backendless.Data.of( Image_management.class ).findLast();
  }

  public static Future<Image_management> findLastAsync()
  {
    if( Backendless.isAndroid() )
    {
      throw new UnsupportedOperationException( "Using this method is restricted in Android" );
    }
    else
    {
      Future<Image_management> future = new Future<Image_management>();
      Backendless.Data.of( Image_management.class ).findLast( future );

      return future;
    }
  }

  public static void findLastAsync( AsyncCallback<Image_management> callback )
  {
    Backendless.Data.of( Image_management.class ).findLast( callback );
  }

  public static BackendlessCollection<Image_management> find( BackendlessDataQuery query )
  {
    return Backendless.Data.of( Image_management.class ).find( query );
  }

  public static Future<BackendlessCollection<Image_management>> findAsync( BackendlessDataQuery query )
  {
    if( Backendless.isAndroid() )
    {
      throw new UnsupportedOperationException( "Using this method is restricted in Android" );
    }
    else
    {
      Future<BackendlessCollection<Image_management>> future = new Future<BackendlessCollection<Image_management>>();
      Backendless.Data.of( Image_management.class ).find( query, future );

      return future;
    }
  }

  public static void findAsync( BackendlessDataQuery query, AsyncCallback<BackendlessCollection<Image_management>> callback )
  {
    Backendless.Data.of( Image_management.class ).find( query, callback );
  }
}