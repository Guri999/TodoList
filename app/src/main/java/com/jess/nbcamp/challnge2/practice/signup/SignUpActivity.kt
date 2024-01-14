package com.jess.nbcamp.challnge2.practice.signup

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import com.jess.nbcamp.challnge2.databinding.SignUpActivityBinding

class SignUpActivity : AppCompatActivity() {

    companion object {

        const val EXTRA_ENTRY_TYPE = "extra_entry_type"
        const val EXTRA_USER_ENTITY = "extra_user_entity"

        fun newIntent(
            context: Context,
            entryType: SignUpEntryType,
            entity: SignUpUserEntity? = null
        ): Intent = Intent(context, SignUpActivity()::class.java).apply {
            putExtra(EXTRA_ENTRY_TYPE, entryType.ordinal)
            putExtra(EXTRA_USER_ENTITY, entity)
        }
    }

    private val binding: SignUpActivityBinding by lazy {
        SignUpActivityBinding.inflate(layoutInflater)
    }

    private val entryType: SignUpEntryType by lazy {
        SignUpEntryType.getEntryType(
            intent?.getIntExtra(EXTRA_ENTRY_TYPE, 0)
        )
    }

    private val userEntity: SignUpUserEntity? by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent?.getParcelableExtra(EXTRA_USER_ENTITY, SignUpUserEntity::class.java)
        } else {
            intent?.getParcelableExtra(EXTRA_USER_ENTITY)
        }
    }

    private val viewModel: SignUpViewModel by viewModels {
        SignUpViewModelFactory(
            context = this@SignUpActivity,
            entryType = entryType,
            userEntity = userEntity
        )
    }

    private val editTexts
        get() = with(binding) {
            listOf(
                etName,
                etEmail,
                etEmailService,
                etPassword,
                etPasswordConfirm
            )
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initView()
        initViewModel()
    }

    private fun initView() = with(binding) {

        // text changed
        setTextChangedListener()

        // focus out 처리
        setOnFocusChangedListener()

        serviceProvider.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) = Unit

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                viewModel.onItemSelectedEmailService(position)
            }
        }

        btConfirm.setOnClickListener {
            viewModel.onClickSignUp()
        }
    }

    private fun initViewModel() = with(viewModel) {
        event.observe(this@SignUpActivity) {
            when (it) {
                is SignUpEvent.VisibleEmailService -> {
                    binding.etEmailService.isVisible = it.visible
                }

                is SignUpEvent.ClickConfirmButton -> {
                    setResult(RESULT_OK,
                        Intent().apply {
                            // TODO 이것 저것
                        }
                    )
                    finish()
                }
            }
        }

        userUiState.observe(this@SignUpActivity) {
            with(binding) {
                etName.setText(it.name)
                etEmail.setText(it.email)
                etEmailService.setText(it.emailService)
                serviceProvider.setSelection(it.emailPosition)
            }
        }

        emailServices.observe(this@SignUpActivity) {
            binding.serviceProvider.adapter = ArrayAdapter(
                this@SignUpActivity,
                android.R.layout.simple_spinner_dropdown_item,
                it
            )
        }

        errorUiState.observe(this@SignUpActivity) {
            if (it == null) {
                return@observe
            }

            with(binding) {
                tvNameError.setText(it.name.message)
                tvEmailError.setText(it.email.message)
                tvEmailError.setText(it.emailService.message)

                with(tvPasswordError) {
                    isEnabled = it.passwordEnabled
                    setText(it.password.message)
                }

                tvPasswordConfirmError.setText(it.passwordConfirm.message)
            }
        }

        buttonUiState.observe(this@SignUpActivity) {
            with(binding.btConfirm) {
                setText(it.text)
                isEnabled = it.enabled
            }
        }
    }

    private fun setTextChangedListener() {
        editTexts.forEach { editText ->
            editText.addTextChangedListener {
                editText.setErrorMessage()
                viewModel.checkConfirmButtonEnable()
            }
        }
    }

    private fun setOnFocusChangedListener() = with(binding) {
        editTexts.forEach { editText ->
            editText.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus.not()) {
                    editText.setErrorMessage()
                }
            }
        }
    }

    private fun EditText.setErrorMessage() = with(binding) {
        when (this@setErrorMessage) {
            etName -> viewModel.checkValidName(etName.text.toString())
            etEmail -> viewModel.checkValidEmail(etEmail.text.toString())
            etEmailService -> viewModel.checkValidEmailService(
                etEmailService.text.toString(),
                etEmailService.isVisible
            )

            etPassword -> viewModel.checkValidPassword(etPassword.text.toString())
            etPasswordConfirm -> viewModel.checkValidPasswordConfirm(
                etPassword.text.toString(),
                etPasswordConfirm.text.toString()
            )

            else -> Unit
        }
    }
}