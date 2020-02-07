package com.halgroithm.contactmanager;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;

public class ContactlistAdapter extends RecyclerView.Adapter<ContactlistAdapter.MyHolder> {
    private Context ctx;
    private ArrayList<Person> AllPersons;

    public ContactlistAdapter(Context ctx, ArrayList<Person> persons) {
        this.ctx = ctx;
        AllPersons = persons;
    }
    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View myOwnView = LayoutInflater.from(ctx).inflate(R.layout.personstemp, parent,false);

        return new MyHolder(myOwnView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, final int position) {
        holder.personname.setText(AllPersons.get(position).firstname +" " + AllPersons.get(position).lastname);
        holder.address.setText(AllPersons.get(position).address);
        holder.phone.setText(AllPersons.get(position).number);
        /*File imageFile = new File(Allpersons.get(position).images);
        Bitmap imageBitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
        holder.image.setImageBitmap(imageBitmap);*/

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View myView) {
                // Toast.makeText(ctx1, "Hey recycler"+ position, Toast.LENGTH_SHORT).show();
                AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
                builder.setTitle(AllPersons.get(position).firstname + AllPersons.get(position).lastname);
                String[] actions = {"Edit","Remove"};
                builder.setItems(actions, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch(i){
                            case 0:
                            {
                               Intent i1 = new Intent(ctx, ViewEditPersonActivity.class);
                                i1.putExtra("editcontact", AllPersons.get(position));
                                ctx.startActivity(i1);
                                break;

                            }
                            case 1:
                            {
                                ContactsSQLite sqLite  =new ContactsSQLite(ctx);
                                sqLite.removedata(AllPersons.get(position).id);
                                AllPersons.remove(position);
                                notifyDataSetChanged();
                                Toast.makeText(ctx,AllPersons.get(position).firstname + AllPersons.get(position).lastname + " deleted",Toast.LENGTH_LONG).show();
                            }
                            break;
                        }

                    }

                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

    }



    @Override
    public int getItemCount() {
        return AllPersons.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        TextView personname, phone, address;
        ImageView image;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            personname = itemView.findViewById(R.id.personname);
            phone = itemView.findViewById(R.id.personphone);
            address = itemView.findViewById(R.id.address);
            image = itemView.findViewById(R.id.proimg);
        }
    }
}
