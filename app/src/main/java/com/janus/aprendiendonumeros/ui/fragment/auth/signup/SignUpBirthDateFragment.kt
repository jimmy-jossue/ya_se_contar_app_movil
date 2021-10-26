package com.janus.aprendiendonumeros.ui.fragment.auth.signup

import android.view.View
import android.widget.ArrayAdapter
import androidx.lifecycle.lifecycleScope
import com.google.firebase.Timestamp
import com.janus.aprendiendonumeros.R
import com.janus.aprendiendonumeros.data.model.User
import com.janus.aprendiendonumeros.databinding.FragmentSignUpBirthDateBinding
import com.janus.aprendiendonumeros.ui.animation.UIAnimations
import com.janus.aprendiendonumeros.ui.base.BaseFragment
import com.janus.aprendiendonumeros.ui.dialog.InformationDialog
import com.janus.aprendiendonumeros.ui.listener.ControllablePager
import com.janus.aprendiendonumeros.ui.listener.ControllablePagerObserver
import com.janus.aprendiendonumeros.ui.utilities.fieldIsEmpty
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class SignUpBirthDateFragment : BaseFragment(R.layout.fragment_sign_up_birth_date),
    ControllablePagerObserver {

    private lateinit var binding: FragmentSignUpBirthDateBinding
    private lateinit var controllablePager: ControllablePager
    private val currentYear = Calendar.getInstance().get(Calendar.YEAR)
    private var birthDate: Timestamp? = null

    companion object {
        private const val CALENDAR_MONTH = 0
    }

    override fun initUI(view: View) {
        binding = FragmentSignUpBirthDateBinding.bind(view)
        addEvents()
        stUpSpinner()
    }

    private fun addEvents() {
        binding.btnForward.setOnClickListener { isSelectedDate() }
        binding.btnBackward.setOnClickListener { controllablePager.backward() }
        binding.btnClearDate.setOnClickListener { clearDate() }
    }

    override fun setControllablePager(controllablePager: ControllablePager) {
        this.controllablePager = controllablePager
    }

    private fun stUpSpinner() {
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.mount_array,
            R.layout.spinner_text
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerMonth.adapter = adapter
        }
    }

    private fun clearDate() {
        binding.etDay.text.clear()
        binding.etDay.error = null
        binding.spinnerMonth.setSelection(CALENDAR_MONTH, true)
        binding.etYear.text.clear()
        binding.etYear.error = null
    }

    private fun isSelectedDate() {
        val dayIsEmpty = binding.etDay.text.isEmpty()
        val monthIsNotSelected = (binding.spinnerMonth.selectedItemPosition == CALENDAR_MONTH)
        val yearIsEmpty = binding.etYear.text.isEmpty()

        if (dayIsEmpty and monthIsNotSelected and yearIsEmpty) {
            controllablePager.forward()
        } else {
            validateDate()
        }
    }

    private fun saveData(day: Int, month: Int, year: Int) {
        birthDate = Timestamp(GregorianCalendar(year, month, day).time)
        User.staticInstance.birthDate = birthDate
        controllablePager.forward()
    }

    private fun validateDate() {
        val day: String = binding.etDay.text.toString().trim()
        val month: Int = binding.spinnerMonth.selectedItemPosition
        val year: String = binding.etYear.text.toString().trim()

        when {
            day.isEmpty() -> binding.etDay.fieldIsEmpty(mContext, "un día")
            validateDay(day.toInt()).not() -> messageInvalidateDate(binding.etDay, "día")
            month.toString().isEmpty() -> binding.etYear.fieldIsEmpty(mContext, "un mes")
            validateMonth(month).not() -> messageInvalidateDate(binding.spinnerMonth, "mes")
            year.isEmpty() -> binding.etYear.fieldIsEmpty(mContext, "un año")
            validateYear(year.toInt()).not() -> messageInvalidateDate(binding.etYear, "año")
            validateIfDateExists(
                day.toInt(), month, year.toInt()
            ).not() -> messageNonExistentDate()
            else -> saveData(day.toInt(), month, year.toInt())
        }
    }

    private fun validateDay(day: Int): Boolean {
        return (day in 1..31)
    }

    private fun validateMonth(month: Int): Boolean {
        return (month in 1..12)
    }

    private fun validateYear(year: Int): Boolean {
        return (year in 1990..currentYear)
    }

    private fun validateIfDateExists(day: Int, month: Int, year: Int): Boolean {
        var validate: Boolean
        try {
            val formatDate = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
            formatDate.isLenient = false
            formatDate.parse("$year/$month/$day")
            validate = true
        } catch (e: ParseException) {
            validate = false
        }
        return validate
    }

    private fun messageInvalidateDate(view: View, date: String) {
        val anim = UIAnimations(requireContext())
        anim.startAnimation(view, R.anim.attention)
        lifecycleScope.launch() {
            delay(500)
            mContext.showDialogInformation(
                icon = InformationDialog.ICON_WARNING,
                title = "¡Ho, no! Valor incorrecto",
                text = "¿Estás seguro que ese $date existe?." +
                        "\n\nRecuerda esto:" +
                        "\n\t● Un día valido es entre 1 y 31." +
                        "\n\t● Solo existen 12 meses." +
                        "\n\t● Debes ingresar un año que esté entre 1990 y $currentYear."
            )
        }
    }

    private fun messageNonExistentDate() {
        mContext.showDialogInformation(
            icon = InformationDialog.ICON_WARNING,
            title = "¡Vaya! Esa fecha no existe",
            text = "Por favor ingresa una fecha correcta."
        )
    }
}