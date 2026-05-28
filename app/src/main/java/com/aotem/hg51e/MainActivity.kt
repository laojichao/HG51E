package com.aotem.hg51e

import android.content.ClipData
import android.content.ClipboardManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.aotem.hg51e.ui.main.MainViewModel
import com.aotem.hg51e.ui.main.UiState
import com.hjq.shape.view.ShapeButton
import com.hjq.shape.view.ShapeEditText
import com.hjq.toast.Toaster
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    companion object {
        private const val DEFAULT_HOST = "192.168.1.1"
    }

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
                            if (state.shouldShowToast) Toaster.showShort("获取成功")
                            etAccount.setText(state.user.data.webTeleAccountName)
                            etPassword.setText(state.user.data.teleAccountPassword)
                        }
                        is UiState.Error -> {
                            btnGet.isEnabled = true
                            btnCopy.isEnabled = true
                            if (state.shouldShowToast) Toaster.showShort("网络请求失败")
                        }
                    }
                }
            }
        }
    }

    private fun onGetClicked() {
        if (!btnGet.isEnabled) return

        val raw = etHost.text.toString().trim().ifEmpty { DEFAULT_HOST }
        val host = if (raw.startsWith("http")) raw else "http://$raw"
        val url = host.trimEnd('/') + "/"

        val network = connectivityManager.activeNetwork
        val capabilities = network?.let { connectivityManager.getNetworkCapabilities(it) }

        if (capabilities == null) {
            Toaster.showShort("当前无网络连接")
            return
        }

        when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN) -> {
                Toaster.showShort("请关闭你的代理")
            }
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                viewModel.queryUser(url)
            }
            else -> {
                Toaster.showShort("当前网络类型不支持")
            }
        }
    }

    private fun onCopyClicked() {
        val password = etPassword.text.toString()
        if (password.isEmpty()) {
            Toaster.showShort("当前密码为空，请先获取密码")
            return
        }
        clipboardManager.setPrimaryClip(ClipData.newPlainText("password", password))
    }
}
