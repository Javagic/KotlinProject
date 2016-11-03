package com.ilya.portfolioproject;

import android.graphics.Rect;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import ru.altarix.dtu_core.model.Many;
import ru.altarix.dtu_core.utils.ApplicationHelper;
import ru.altarix.dtu_core.utils.ButtonLocker;
import ru.altarix.umort.Event;
import ru.altarix.umort.component.view.EditTextWarning;
import ru.altarix.umort.core.utils.NotebookRecyclerAdapter;
import ru.altarix.umort.core.utils.RecyclerDialogFragment;
import ru.altarix.umort.model.Violation;
import ru.altarix.umort.model.ViolationItem;
import ru.altarix.umort.ui.violation.ViolationCardActivity;
import ru.altarix.umort.ui.violation.fragment.base.BaseCardViolationFragment;

/**
 * Created by paul on 12/22/15.
 */
@EFragment(R.layout.f_violation)
public class ViolationFragment extends BaseCardViolationFragment
    implements ViolationListRecyclerAdapter.OnRecycleCallback {
  @ViewById(R.id.wrapper)
  protected View wrapper;
  @ViewById(R.id.wrapper_item)
  protected View wrappeItem;
  @ViewById(R.id.comment_container)
  LinearLayout commentContainer;
  @ViewById(R.id.cb_violation_revealed)
  CheckBox checkBox;
  @ViewById(R.id.violation_recycler_list)
  RecyclerView recyclerView;
  @ViewById(R.id.agentName)
  EditText et_agentName;
  @ViewById(R.id.agentPost)
  EditText et_agentPost;
  @ViewById(R.id.et_comment)
  EditText et_comment;
  ViolationListRecyclerAdapter recyclerAdapter;

  RecyclerDialogFragment recyclerDialogFragment;

  @Override
  public void updateWarnUI(boolean warn) {
    recyclerAdapter.updateWarnUi(warn);
  }

  @Override
  public void init() {
    super.init();
    et_agentName.setText(violation.agentName);
    et_agentPost.setText(violation.agentPost);
    et_comment.setText(violation.comment);
    recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
      @Override
      public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
          RecyclerView.State state) {
        if (view instanceof RelativeLayout) {
          outRect.set(0, 0, 0, 10);
        }
      }
    });
    et_agentName.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
      }

      @Override
      public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
      }

      @Override
      public void afterTextChanged(Editable editable) {
        getViolation().agentName = et_agentName.getText().toString();
      }
    });
    et_agentPost.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
      }

      @Override
      public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
      }

      @Override
      public void afterTextChanged(Editable editable) {
        getViolation().agentPost = et_agentPost.getText().toString();
      }
    });
    et_comment.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
      }

      @Override
      public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

      }

      @Override
      public void afterTextChanged(Editable editable) {
        violation.comment = et_comment.getText().toString();
      }
    });
    if (violation.inspectionResult.equals(Violation.VIOLATION_NOT_DETECTED)) {
      checkBox.setChecked(true);
      commentContainer.setVisibility(View.VISIBLE);
    } else {
      recyclerAdapter =
          new ViolationListRecyclerAdapter(getViolation().violations, getViolation().umortObject,
              this);
      recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));
      recyclerView.setAdapter(recyclerAdapter);
    }
  }

  @Override
  public void handleStatus() {
    switch (violation.status) {
      case Violation.Status.EXPORTED: {
        /**
         * отключить удаление
         * отключить добавление
         * отключить блокнот
         */

        if (violation.inspectionResult.equals(Violation.VIOLATION_NOT_DETECTED)) {
          checkBox.setChecked(true);
          checkBox.setEnabled(false);
          et_agentPost.setFocusable(false);
          et_agentPost.setFocusableInTouchMode(false);
          et_agentName.setFocusable(false);
          et_agentName.setFocusableInTouchMode(false);
        } else {
          checkBox.setVisibility(View.GONE);
          ((EditTextWarning) getActivity().findViewById(
              R.id.violationDescription)).setCompoundDrawables(null, null, null, null);
          getActivity().findViewById(R.id.delete_violation_view).setVisibility(View.GONE);
        }
        break;
      }
    }
  }

  @Override
  public boolean checkRequired() {
    if (getViolation().inspectionResult.equals(Violation.VIOLATION_NOT_DETECTED)) return true;
    if (checkBox.isChecked() || checkBox.getVisibility() == View.GONE) return true;
    if (getViolation().inspectionResult.equals(Violation.VIOLATION_DETECTED)
        && getViolation().violations.isEmpty()) {
      return false;
    }
    return recyclerAdapter.checkRequired();
  }

  @Override
  public void onViolationName(int position) {
    if (ButtonLocker.tryLock()) {
      List<ViolationItem> viols = ViolationItem.loadAll();
      FragmentManager fm = ((ViolationCardActivity) getContext()).getSupportFragmentManager();
      recyclerDialogFragment = RecyclerDialogFragment.newInstance(viols, 1,
          new NotebookRecyclerAdapter.onNotebookCallback<ViolationItem>() {
            @Override
            public void onDelete(int position) {
            }

            @Override
            public void onNotebookResult(ViolationItem value) {
              ViolationItem violationItem = violation.violations.get(position);
              violationItem.name = value.name;
              if (value.getId() != -1L) violationItem.setAaId(value.getId());//что оно делает?
              recyclerDialogFragment.dismiss();
              recyclerAdapter.notifyItemChanged(position);
              ApplicationHelper.otto.post(
                  new Event.ViolationActUpdated(Event.ViolationActUpdated.What.VIOLATION));
              ApplicationHelper.otto.post(
                  new Event.ViolationActUpdated(Event.ViolationActUpdated.What.UPDATE_REQUIRED));
            }
          }, "Тип нарушения");
      recyclerDialogFragment.show(fm, "notebook_dialog");
    }
  }

  @Override
  public void onViolationDescription(int position, View view) {
    PopupMenu popup = new PopupMenu(getContext(), view, Gravity.RIGHT);
    popup.getMenuInflater().inflate(R.menu.menu_notebook_file, popup.getMenu());
    popup.setOnMenuItemClickListener(menuItem -> {
      if (menuItem.getItemId() != R.id.download_doc) {
        List<String> stringList = new ArrayList<String>();
        stringList.add("dgdgddd");
        stringList.add("dgdgddd");
        stringList.add("dgdgSDSDFSFSSDSDQÅBVVVVBCXBSFSFFFFNFNFFSSSĀddd");
        stringList.add("dgdgddd");
        stringList.add("dgdgddd");
        stringList.add("dgdgddd");
        FragmentManager fm = ((ViolationCardActivity) getContext()).getSupportFragmentManager();
        recyclerDialogFragment = RecyclerDialogFragment.newInstance(stringList, 0,
            new NotebookRecyclerAdapter.onNotebookCallback<String>() {
              @Override
              public void onDelete(int pos) {

                stringList.remove(pos);
              }

              @Override
              public void onNotebookResult(String value) {
                recyclerDialogFragment.dismiss();
                violation.violations.get(position).description = value;
                recyclerAdapter.notifyItemChanged(position);
                ApplicationHelper.otto.post(
                    new Event.ViolationActUpdated(Event.ViolationActUpdated.What.VIOLATION));
                ApplicationHelper.otto.post(
                    new Event.ViolationActUpdated(Event.ViolationActUpdated.What.UPDATE_REQUIRED));
              }
            }, "Блокнот");
        recyclerDialogFragment.show(fm, "notebook_dialog");
      } else {
        //загрузить
      }
      return false;
    });
    setForceShowIcon(popup);
    popup.show();
  }

  @Click({ R.id.cb_violation_revealed })
  protected void onChangeCheckBox(View view) {
    if (((CheckBox) view).isChecked()) {
      if (!violation.violations.isEmpty()) {
        savedViolations = new Many<ViolationItem>();
        for (ViolationItem violationItem : getViolation().violations) {
          savedViolations.add(violationItem);
        }
        getViolation().violations.clear();
      }
      recyclerView.setVisibility(View.GONE);
      commentContainer.setVisibility(View.VISIBLE);
      getViolation().inspectionResult = Violation.VIOLATION_NOT_DETECTED;
      ApplicationHelper.otto.post(
          new Event.ViolationActUpdated(Event.ViolationActUpdated.What.UPDATE_REQUIRED));
      ApplicationHelper.otto.post(
          new Event.ViolationActUpdated(Event.ViolationActUpdated.What.VIOLATION));
    } else {
      if (savedViolations != null) {
        for (ViolationItem savedViolation : savedViolations) {
          violation.violations.add(savedViolation);
        }
      }
      getViolation().inspectionResult = Violation.VIOLATION_DETECTED;
      recyclerView.setVisibility(View.VISIBLE);
      commentContainer.setVisibility(View.GONE);
      recyclerAdapter = new ViolationListRecyclerAdapter(getViolation().violations, getViolation().umortObject, this);
      recyclerView.setLayoutManager(
          new android.support.v7.widget.LinearLayoutManager(getContext()));
      recyclerView.setAdapter(recyclerAdapter);
      ApplicationHelper.otto.post(
          new Event.ViolationActUpdated(Event.ViolationActUpdated.What.UPDATE_REQUIRED));
      ApplicationHelper.otto.post(
          new Event.ViolationActUpdated(Event.ViolationActUpdated.What.VIOLATION));
    }
  }

  public static void setForceShowIcon(PopupMenu popupMenu) {
    try {
      Field[] fields = popupMenu.getClass().getDeclaredFields();
      for (Field field : fields) {
        if ("mPopup".equals(field.getName())) {
          field.setAccessible(true);
          Object menuPopupHelper = field.get(popupMenu);
          Class<?> classPopupHelper = Class.forName(menuPopupHelper.getClass().getName());
          Method setForceIcons = classPopupHelper.getMethod("setForceShowIcon", boolean.class);
          setForceIcons.invoke(menuPopupHelper, true);
          break;
        }
      }
    } catch (Throwable e) {
      e.printStackTrace();
    }
  }

  @Override
  public Violation getViolation() {
    return violation != null ? violation : getViolationHolder().getViolation();
  }
}
