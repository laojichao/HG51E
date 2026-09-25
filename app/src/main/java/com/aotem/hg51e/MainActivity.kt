package com.aotem.hg51e

import android.content.ClipData
import android.content.ClipDescription
import android.content.ClipboardManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.aotem.hg51e.data.remote.HostUrl
import com.aotem.hg51e.ui.main.MainViewModel
import com.aotem.hg51e.ui.main.UiState
import com.hjq.shape.view.ShapeButton
import com.hjq.shape.view.ShapeEditText
import com.hjq.toast.Toaster
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()

    private lateinit var etAccount: ShapeEditText
    private lateinit var etPassword: ShapeEditText
    private lateinit var etHost: ShapeEditText
    private lateinit var btnGet: ShapeButton
    private lateinit var btnCopy: ShapeButton

    private val clipboardManager: ClipboardManager by lazy {
        getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
    }
    private val connectivityManager: ConnectivityManager by lazy {
        getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etAccount = findViewById(R.id.et_account)
        etPassword = findViewById(R.id.et_password)
        etHost = findViewById(R.id.et_host)
        btnGet = findViewById(R.id.btn_get)
        btnCopy = findViewById(R.id.btn_copy)

        btnGet.setOnClickListener { onGetClicked() }
        btnCopy.setOnClickListener { onCopyClicked() }

        observeUiState()
        observeMessages()
    }

    private fun observeUiState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    when (state) {
                        is UiState.Idle -> Unit
                        is UiState.Loading -> {
                            btnGet.isEnabled = false
                            btnCopy.isEnabled = false
                        }
                        is UiState.Success -> {
                            btnGet.isEnabled = true
                            btnCopy.isEnabled = true
                            etAccount.setText(state.user.data.webTeleAccountName)
                            etPassword.setText(state.user.data.teleAccountPassword)
                        }
                        is UiState.Error -> {
                            btnGet.isEnabled = true
                            btnCopy.isEnabled = true
                        }
                    }
                }
            }
        }
    }

    private fun observeMessages() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.messages.collect { Toaster.showShort(it) }
            }
        }
    }

    private fun onGetClicked() {
        if (!btnGet.isEnabled) return

        val baseUrl = HostUrl.normalize(etHost.text?.toString())
        if (baseUrl == null) {
            Toaster.showShort("地址格式不正确")
            return
        }

        val capabilities = connectivityManager.activeNetwork
            ?.let { connectivityManager.getNetworkCapabilities(it) }
        if (capabilities == null) {
            Toaster.showShort("当前无网络连接")
            return
        }

        when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN) ->
                Toaster.showShort("请关闭你的代理")
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) ->
                viewModel.queryUser(baseUrl.toString())
            else ->
                Toaster.showShort("当前网络类型不支持")
        }
    }

    private fun onCopyClicked() {
        val password = etPassword.text?.toString().orEmpty()
        if (password.isEmpty()) {
            Toaster.showShort("当前密码为空，请先获取密码")
            return
        }
        val clip = ClipData.newPlainText("password", password)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            clip.description.extras = PersistableBundle().apply {
                putBoolean(ClipDescription.EXTRA_IS_SENSITIVE, true)
            }
        }
        clipboardManager.setPrimaryClip(clip)
        Toaster.showShort("已复制到剪贴板")
    }
}
