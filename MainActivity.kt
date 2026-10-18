package com.oxide.demo

import android.app.Activity
import android.os.Bundle
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.content.Context
import android.view.Gravity
import android.view.View
import android.widget.*

class MainActivity : Activity() {
    private val bg = Color.rgb(3, 10, 22)
    private val panel = Color.rgb(7, 22, 42)
    private val cyan = Color.rgb(0, 169, 255)
    private val pale = Color.rgb(222, 240, 255)
    private val prefs by lazy { getSharedPreferences("oxide_demo", Context.MODE_PRIVATE) }
    private lateinit var root: LinearLayout
    private lateinit var espPlayers: Switch
    private lateinit var espSkeleton: Switch
    private lateinit var espLoot: Switch
    private lateinit var espVehicles: Switch
    private lateinit var aimDemo: Switch
    private lateinit var visibility: Switch
    private lateinit var ignoreAllies: Switch
    private lateinit var fovSeek: SeekBar
    private lateinit var fovValue: TextView
    private lateinit var language: Spinner
    private var english = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = bg
        window.navigationBarColor = bg
        buildUi()
        loadConfig(false)
    }

    private fun dp(v: Int) = (v * resources.displayMetrics.density).toInt()
    private fun card(): LinearLayout = LinearLayout(this).apply {
        orientation = LinearLayout.VERTICAL
        setPadding(dp(16), dp(14), dp(16), dp(14))
        background = GradientDrawable().apply {
            setColor(panel); cornerRadius = dp(16).toFloat()
            setStroke(dp(1), Color.rgb(0, 91, 164))
        }
        val p = LinearLayout.LayoutParams(-1, -2)
        p.setMargins(0, 0, 0, dp(12))
        layoutParams = p
    }
    private fun label(text: String, size: Float = 15f, color: Int = pale, bold: Boolean = false) =
        TextView(this).apply {
            this.text = text; textSize = size; setTextColor(color)
            if (bold) setTypeface(typeface, Typeface.BOLD)
            setPadding(0, dp(4), 0, dp(6))
        }
    private fun addSwitch(parent: LinearLayout, title: String, key: String, default: Boolean = false): Switch {
        val sw = Switch(this).apply {
            text = title; textSize = 15f; setTextColor(pale)
            isChecked = prefs.getBoolean(key, default)
            buttonTintList = android.content.res.ColorStateList.valueOf(cyan)
            setPadding(0, dp(7), 0, dp(7))
        }
        parent.addView(sw, LinearLayout.LayoutParams(-1, -2))
        return sw
    }
    private fun heading(parent: LinearLayout, title: String, subtitle: String) {
        parent.addView(label(title, 20f, cyan, true))
        parent.addView(label(subtitle, 13f, pale))
        val line = View(this).apply { setBackgroundColor(Color.rgb(0, 75, 130)) }
        parent.addView(line, LinearLayout.LayoutParams(-1, dp(1)))
    }
    private fun buildUi() {
        root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL; setBackgroundColor(bg)
        }
        val scroll = ScrollView(this)
        val content = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(16), dp(14), dp(16), dp(22))
        }
        scroll.addView(content)
        root.addView(scroll, LinearLayout.LayoutParams(-1, 0, 1f))
        setContentView(root)

        content.addView(label("✦ OXIDE", 30f, cyan, true))
        content.addView(label("SURVIVAL ISLAND  •  PAINEL DEMO", 13f, pale, true))
        content.addView(label("Interface de demonstração — não altera o jogo.", 13f, Color.LTGRAY))
        val notice = card()
        notice.addView(label("MODO SEGURO / DEMONSTRAÇÃO", 14f, cyan, true))
        notice.addView(label("Os controles abaixo são apenas visuais. Não há mira automática, ESP real, injeção ou modificação do jogo.", 13f))
        content.addView(notice)

        val esp = card()
        heading(esp, "◉  ESP (visual)", "Pré-visualização de opções de marcação")
        espPlayers = addSwitch(esp, "ESP jogadores (demo)", "esp_players", true)
        espSkeleton = addSwitch(esp, "ESP esqueleto (demo)", "esp_skeleton")
        espLoot = addSwitch(esp, "Caixas / itens (demo)", "esp_loot", true)
        espVehicles = addSwitch(esp, "Veículos (demo)", "esp_vehicles")
        esp.addView(label("Legenda: 🔴 jogadores    🟢 aliados    🟡 itens", 12f, Color.LTGRAY))
        content.addView(esp)

        val aim = card()
        heading(aim, "⊕  Aimbot (demonstrativo)", "Ajustes fictícios; não controlam a mira")
        aimDemo = addSwitch(aim, "Ativar visualização de Aimbot", "aim_demo")
        visibility = addSwitch(aim, "Verificação de visibilidade (demo)", "visibility")
        ignoreAllies = addSwitch(aim, "Ignorar aliados (demo)", "ignore_allies", true)
        aim.addView(label("FOV do Aimbot: somente indicador", 14f))
        fovValue = label("30°", 16f, cyan, true)
        aim.addView(fovValue)
        fovSeek = SeekBar(this).apply { max = 120; progress = 30; progressTintList = android.content.res.ColorStateList.valueOf(cyan); thumbTintList = android.content.res.ColorStateList.valueOf(cyan) }
        fovSeek.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(s: SeekBar?, p: Int, fromUser: Boolean) { fovValue.text = "$p°" }
            override fun onStartTrackingTouch(s: SeekBar?) {}
            override fun onStopTrackingTouch(s: SeekBar?) {}
        })
        aim.addView(fovSeek)
        content.addView(aim)

        val fov = card()
        heading(fov, "◎  Campo de visão", "Controle visual independente")
        fov.addView(label("FOV geral: 120°", 14f, pale))
        val fovGeneral = SeekBar(this).apply { max = 180; progress = 120; progressTintList = android.content.res.ColorStateList.valueOf(cyan); thumbTintList = android.content.res.ColorStateList.valueOf(cyan) }
        fov.addView(fovGeneral)
        content.addView(fov)

        val langCard = card()
        heading(langCard, "◎  Idioma", "Idioma dos rótulos principais")
        language = Spinner(this).apply {
            adapter = ArrayAdapter(this@MainActivity, android.R.layout.simple_spinner_dropdown_item, listOf("Português (Brasil)", "English"))
            setSelection(if (prefs.getBoolean("english", false)) 1 else 0)
            setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {}
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    english = position == 1
                }
            })
        }
        langCard.addView(language)
        content.addView(langCard)

        val config = card()
        heading(config, "☁  Configurações", "Salve e restaure as preferências deste painel")
        val save = Button(this).apply { text = "Salvar configuração"; setOnClickListener { saveConfig(); toast("Configuração salva neste aparelho.") } }
        val load = Button(this).apply { text = "Carregar configuração"; setOnClickListener { loadConfig(true) } }
        val reset = Button(this).apply { text = "Restaurar padrão"; setOnClickListener { prefs.edit().clear().apply(); loadConfig(false); toast("Padrões restaurados.") } }
        config.addView(save); config.addView(load); config.addView(reset)
        content.addView(config)

        content.addView(label("OXIDE PANEL  •  v1.0.0 DEMO", 12f, Color.GRAY))
    }

    private fun saveConfig() {
        prefs.edit()
            .putBoolean("esp_players", espPlayers.isChecked)
            .putBoolean("esp_skeleton", espSkeleton.isChecked)
            .putBoolean("esp_loot", espLoot.isChecked)
            .putBoolean("esp_vehicles", espVehicles.isChecked)
            .putBoolean("aim_demo", aimDemo.isChecked)
            .putBoolean("visibility", visibility.isChecked)
            .putBoolean("ignore_allies", ignoreAllies.isChecked)
            .putInt("aim_fov", fovSeek.progress)
            .putBoolean("english", language.selectedItemPosition == 1)
            .apply()
    }
    private fun loadConfig(showToast: Boolean) {
        espPlayers.isChecked = prefs.getBoolean("esp_players", true)
        espSkeleton.isChecked = prefs.getBoolean("esp_skeleton", false)
        espLoot.isChecked = prefs.getBoolean("esp_loot", true)
        espVehicles.isChecked = prefs.getBoolean("esp_vehicles", false)
        aimDemo.isChecked = prefs.getBoolean("aim_demo", false)
        visibility.isChecked = prefs.getBoolean("visibility", false)
        ignoreAllies.isChecked = prefs.getBoolean("ignore_allies", true)
        fovSeek.progress = prefs.getInt("aim_fov", 30)
        language.setSelection(if (prefs.getBoolean("english", false)) 1 else 0)
        if (showToast) toast("Configuração carregada.")
    }
    private fun toast(msg: String) = Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}
