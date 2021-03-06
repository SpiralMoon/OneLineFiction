package com.dsm.onelinefiction;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Database {

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private static Database instance;
    private RecyclerView.Adapter adapter;
    private ArrayList<Item> dataSet;
    private ProgressBar progressBar;

    public Database(String userId) {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference(userId);
        databaseReference.addValueEventListener(valueEventListener);
    }

    public static Database getInstance(String userId) {
        if (instance == null)
            instance = new Database(userId);

        return instance;
    }

    public void checkFirst() {

    }

    //새로운 일기 작성
    public void createNewBook(Page page) {
        Book book = Book.getInstance();
        book.addPage(page);
        databaseReference.setValue(book);
    }

    //기존 일기 내용 수정
    public void modifyBook(Page page, int index) {
        if(index != -1) {
            Book book = Book.getInstance();
            book.pageList.set(index, page);
            databaseReference.setValue(book);
        }
    }

    //일기 삭제
    public void removeBook(int index) {
        Book book = Book.getInstance();
        book.pageList.remove(index);
        databaseReference.setValue(book);
    }

    //메인 액티비티 리사이클러뷰 어댑터
    public void setMainViewUpdate(RecyclerView.Adapter adapter, ArrayList<Item> dataSet, ProgressBar progressBar) {
        this.adapter = adapter;
        this.dataSet = dataSet;
        this.progressBar = progressBar;
    }

    private ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            Book book = dataSnapshot.getValue(Book.class);
            Book.getInstance().pageList = book.pageList;

            //프로그레스바 중지
            progressBar.setVisibility(View.INVISIBLE);
            //데이터가 업데이트되면 메인 액티비티에 수정된 내용을 업데이트하여 보여줌
            for (int i = 0; i < dataSet.size(); i++)
                adapter.notifyItemRemoved(i);
            dataSet.clear();

            for (int j = Book.getInstance().pageList.size() - 1; j >= 0; j--) //최신 내용을 제일 먼저 표시함
                dataSet.add(new Item(Book.getInstance().pageList.get(j).page_title, R.mipmap.ic_launcher));

            adapter.notifyDataSetChanged();
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };
}
