package com.example.tfa;


import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class toilet_Public extends Fragment implements OnMapReadyCallback {

    GoogleMap mMap;
    Location currentLocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("toilets").child("public");
    Double lat,lng;
    String key;
    LatLng location;
    BottomSheetDialog bottomSheetDialog;
    private HashMap<String, Marker> mMarkers = new HashMap<>();
    FirebaseRecyclerOptions<recycleModel> options;
    FirebaseRecyclerAdapter<recycleModel,viewHolder> adapter1;

    private static final int REQUEST_CODE = 101;

    public toilet_Public() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_toilet__public, container, false);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fetchLastLocation();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
//        LatLng sydney = new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude());
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Current Location"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
//        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney,2));
        subscribeToUpdates();
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(final Marker marker) {
                View v = LayoutInflater.from(getContext()).inflate(R.layout.bottomsheet,null);
                bottomSheetDialog = new BottomSheetDialog(getContext());
                final RecyclerView recyclerView = v.findViewById(R.id.recyclerView);
                final RecyclerView recyclerView1 = v.findViewById(R.id.recyclerView2);
                recyclerView1.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                recyclerView1.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,true));
                myRef.child(marker.getTitle()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        final HashMap<String, Object> value = (HashMap<String, Object>) dataSnapshot.getValue();
                        String services = (String) value.get("services").toString();
                        String available = (String) value.get("Available_for").toString();
                        String landmark = (String) value.get("Landmark").toString();
                        String loc = (String) value.get("Location").toString();
                        String type = (String) value.get("Type").toString();
                        String tow = (String) value.get("Type_of_washroom").toString();
                        String dim = (String) value.get("doors_in_mens").toString();
                        String es = (String) value.get("extra_services").toString();
                        String paid = (String) value.get("paid").toString();
                        String sm = value.get("sink_men").toString();
                        String sim = (String) value.get("stalls_in_mens").toString();
                        String siw = (String) value.get("stalls_in_women").toString();
                        String urinals = (String) value.get("urinals").toString();
                        String id = (String) value.get("id").toString();
                        List<String> myList = new ArrayList<String>();
                        myList.add("Services");
                        if (!services.isEmpty())
                        {
                            myList.add("Services : "+services);
                        }if(!es.isEmpty()){
                            myList.add("Extra Services : "+es);
                        }if (!available.isEmpty())
                        {
                            myList.add("Available For : "+available);
                        }if (!landmark.isEmpty()) {
                            myList.add("Landmark : "+landmark);
                        }if (!loc.isEmpty()){
                            myList.add("Location : "+loc);
                        }if(!type.isEmpty()) {
                            myList.add("Type : "+type);
                        }if(!tow.isEmpty()){
                            myList.add("Type of Washroom : "+tow);
                        }if (!paid.isEmpty()){
                            myList.add("Paid : "+paid);
                        }if(!sm.isEmpty()){
                            myList.add("Sink in Men : "+sm);
                        }if(!sim.isEmpty()){
                            myList.add("Stalls in Men : "+sim);
                        }if(!siw.isEmpty()){
                            myList.add("Stalls in Women : "+siw);
                        }if(!urinals.isEmpty()){
                            myList.add("Urinals : "+urinals);
                        }
//                        String[] data = {"Services : "+services,"Available : "+available,"Landmarak : "+landmark,loc,type,tow,dim,es,paid,sm,sim,siw,urinals,id};

//                        Image slider
                DatabaseReference mref = database.getReference().child("toilets").child("public").child(marker.getTitle()).child("links");


                        options = new FirebaseRecyclerOptions.Builder<recycleModel>().setQuery(mref,recycleModel.class).build();

                        adapter1 = new FirebaseRecyclerAdapter<recycleModel, viewHolder>(options) {
                            @Override
                            protected void onBindViewHolder(viewHolder holder, int position,recycleModel model) {
//
                                Picasso.get().load(model.getImage()).into(holder.mimageView, new Callback() {
                                    @Override
                                    public void onSuccess() {

                                    }

                                    @Override
                                    public void onError(Exception e) {
                                        Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                                    }
                                });

                            }

                            @Override
                            public viewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
                                View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row, viewGroup, false);
                                return new viewHolder(v);
                            }
                        };

                        adapter1.startListening();
                        recyclerView1.setAdapter(adapter1);

//                        Image slider

                        recyclerView.setAdapter(new adapter(myList));
//                        Log.d("TAG", "Value is: " + services);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                bottomSheetDialog.setContentView(v);
                bottomSheetDialog.show();


                return true;
            }
        });
    }


    private void subscribeToUpdates() {

        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                setMarker(dataSnapshot);

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                setMarker(dataSnapshot);

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }


        });
    }
    private void setMarker(DataSnapshot dataSnapshot) {
        // When a location update is received, put or update
        // its value in mMarkers, which contains all the markers
        // for locations received, so that we can build the
        // boundaries required to show them all on the map at once
        key = dataSnapshot.getKey();
        final HashMap<String, Object> value = (HashMap<String, Object>) dataSnapshot.getValue();
        lat = Double.parseDouble(value.get("latitude").toString());
        lng = Double.parseDouble(value.get("longitude").toString());

        if(lat!=null && lng!=null){
            location = new LatLng(lat, lng);
            if (!mMarkers.containsKey(key)) {
                mMarkers.put(key, mMap.addMarker(new MarkerOptions().title(key).position(location)));
            } else {
                mMarkers.get(key).setPosition(location);
            }
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            for (Marker marker : mMarkers.values()) {
                builder.include(marker.getPosition());
            }
//            mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 300));
        }
        else {

        }

    }

    private void fetchLastLocation() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE);
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null){
                    currentLocation = location;
                    SupportMapFragment mapFragment = (SupportMapFragment)getChildFragmentManager().findFragmentById(R.id.public_toilet);
                    mapFragment.getMapAsync(toilet_Public.this);
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                fetchLastLocation();
            }
        }
    }


}
