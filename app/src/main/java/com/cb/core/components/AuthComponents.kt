package com.cb.core.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.cb.core.theme.AuthBrandColor
import com.cb.core.theme.AuthTextFieldBorder
import com.cb.core.theme.Dimensions

@Composable
fun LabeledAuthField(
    title: String,
    placeholder: String,
    text: String,
    onValueChange: (String) -> Unit,
    keyboardType: KeyboardType = KeyboardType.Text,
    enabled: Boolean = true,
    isError: Boolean = false,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(Dimensions.LabelSpacing)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
        
        BasicTextField(
            value = text,
            onValueChange = onValueChange,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            textStyle = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onBackground),
            singleLine = true,
            enabled = enabled,
            cursorBrush = SolidColor(AuthBrandColor),
            decorationBox = { innerTextField ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White, shape = RoundedCornerShape(Dimensions.CornerRadiusDefault))
                        .border(
                            width = 1.dp,
                            color = if (isError) Color.Red else AuthTextFieldBorder,
                            shape = RoundedCornerShape(Dimensions.CornerRadiusDefault)
                        )
                        .padding(Dimensions.PaddingMedium),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(modifier = Modifier.weight(1f)) {
                        if (text.isEmpty()) {
                            Text(
                                text = placeholder,
                                style = MaterialTheme.typography.bodyLarge,
                                color = Color.Gray
                            )
                        }
                        innerTextField()
                    }
                }
            }
        )
    }
}

@Composable
fun SecureAuthField(
    title: String,
    placeholder: String,
    text: String,
    onValueChange: (String) -> Unit,
    enabled: Boolean = true,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(Dimensions.LabelSpacing)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
        
        BasicTextField(
            value = text,
            onValueChange = onValueChange,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = PasswordVisualTransformation(),
            textStyle = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onBackground),
            singleLine = true,
            enabled = enabled,
            cursorBrush = SolidColor(AuthBrandColor),
            decorationBox = { innerTextField ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White, shape = RoundedCornerShape(Dimensions.CornerRadiusDefault))
                        .border(
                            width = 1.dp,
                            color = AuthTextFieldBorder,
                            shape = RoundedCornerShape(Dimensions.CornerRadiusDefault)
                        )
                        .padding(Dimensions.PaddingMedium),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(modifier = Modifier.weight(1f)) {
                        if (text.isEmpty()) {
                            Text(
                                text = placeholder,
                                style = MaterialTheme.typography.bodyLarge,
                                color = Color.Gray
                            )
                        }
                        innerTextField()
                    }
                }
            }
        )
    }
}

@Composable
fun PrimaryAuthButton(
    text: String,
    onClick: () -> Unit,
    isEnabled: Boolean = true,
    isLoading: Boolean = false,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        enabled = isEnabled && !isLoading,
        colors = ButtonDefaults.buttonColors(
            containerColor = AuthBrandColor,
            disabledContainerColor = AuthBrandColor.copy(alpha = 0.5f),
            contentColor = Color.White,
            disabledContentColor = Color.White
        ),
        shape = RoundedCornerShape(Dimensions.CornerRadiusDefault),
        contentPadding = PaddingValues(vertical = Dimensions.ButtonVerticalPadding),
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = if (isEnabled && !isLoading) Dimensions.ShadowRadius else 0.dp,
                shape = RoundedCornerShape(Dimensions.CornerRadiusDefault),
                spotColor = AuthBrandColor,
                ambientColor = AuthBrandColor
            )
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                color = Color.White,
                modifier = Modifier.size(24.dp),
                strokeWidth = 2.dp
            )
        } else {
            Text(
                text = text,
                style = MaterialTheme.typography.titleLarge
            )
        }
    }
}
