package net.kary.androidfacebooka;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.share.model.ShareContent;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private static final String EMAIL = "email";
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private ShareDialog shareDialog;
    private ImageView image;
    Uri path;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Compartir foto
        image=findViewById(R.id.imageView);
        Button btnShareImage;
        btnShareImage= (Button) findViewById(R.id.ShareImage);
        btnShareImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharePhoto photo = new SharePhoto.Builder()
                        .setImageUrl(path)
                        .build();
                SharePhotoContent content = new SharePhotoContent.Builder()
                        .addPhoto(photo)
                        .build();
                ShareDialog.show(MainActivity.this, content);
            }
        });
        //Iniciar sesion
        callbackManager = CallbackManager.Factory.create();
        shareDialog=new ShareDialog(this);
        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList(EMAIL));
        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                //startActivity(new Intent(""));
                Toast.makeText(MainActivity.this,"Acceso exitoso",
                        Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onCancel() {
                // App code
            }
            @Override
            public void onError(FacebookException exception) {
                // App code
                Toast.makeText(MainActivity.this,"Error para iniciar",
                        Toast.LENGTH_SHORT).show();
            }

        });



    }

     public void publicarEnlace(View view){

        try {
            if (ShareDialog.canShow(ShareLinkContent.class)) {
                ShareLinkContent linkContent = new ShareLinkContent.Builder()
                        .setContentUrl(Uri.parse("https://www.youtube.com/watch?v=dmW68lzaaqs&ab_channel=PauloLondra"))
                        .build();
                shareDialog.show(linkContent);
            }
        }catch (Exception e){
            Toast.makeText(this, "Error en metodo publicarEnlace: "+e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            path=data.getData();
            image.setImageURI(path);
        }
    }

    public void cargarImagen(View view){
        Intent intent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/");
        startActivityForResult(intent.createChooser(intent, "Seleccione la imagen"), 10);
    }

}